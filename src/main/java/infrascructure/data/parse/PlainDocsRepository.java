/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package infrascructure.data.parse;

import infrascructure.data.Config;
import infrascructure.data.PlainTextResource;
import infrascructure.data.Resource;
import infrascructure.data.list.BigList;
import infrascructure.data.readers.CacheableReader;
import infrascructure.data.readers.SimpleCachedList;
import infrascructure.data.serialize.PlainTextResourceSerializer;
import infrascructure.data.serialize.SerializersFactory;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/**
 * @author shredinger
 */
public class PlainDocsRepository extends CacheableReader<PlainTextResource> {


    private final int MAX_CACHE_SIZE = 100;
    private final String REPOSITORY_STATE_FILE = "last_index.txt";

    private String sourceDir;
    private volatile BigList<PlainTextResource> docs;

    @Autowired
    private Parser parser;

    @Autowired
    private Config config;

    private HashSet<String> uniqueTitles;

    private int required_docs_count;

    private CacheableReader<Resource> resourcesRepository;

    /**
     *
     */
    public PlainDocsRepository(CacheableReader<Resource> resourcesRepository) {
        this.resourcesRepository = resourcesRepository;
    }

    @PostConstruct
    private void init() throws IOException {
        required_docs_count = config.getPropertyInt(Config.REQUIRED_DOCS_COUNT);
        sourceDir = config.getProperty(Config.PLAINDOCS_DIR);
        String tittlesFileName = config.getProperty(Config.TITTLES_PATH);
        PlainTextResourceSerializer serializer = SerializersFactory.createPlainTextSerializer(sourceDir, tittlesFileName);
        docs = new SimpleCachedList<PlainTextResource>(sourceDir, MAX_CACHE_SIZE, serializer);
        initTitles();
    }

    private void initTitles() throws IOException {
        List<String> titles = IOHelper.readLinesFromFile(config.getProperty(Config.TITTLES_PATH));
        uniqueTitles = new HashSet<>(titles);
        if(titles.size() != uniqueTitles.size()){
            Trace.trace("WARNING: DUPLICATED ITEMS (all count = " + titles.size() + "; unique count = " + uniqueTitles.size() );
        }
    }


    /* (non-Javadoc)
     * @see infrascructure.data.readers.CacheableReader#get(java.lang.Integer)
     */
    @Override
    public PlainTextResource get(Integer i) {
        return i >= required_docs_count ? null : docs.get(i);
    }

    /* (non-Javadoc)
     * @see infrascructure.data.readers.CacheableReader#readAll()
     */
    @Override
    public void readAll() throws IOException {
        Boolean parse = Boolean.parseBoolean(config.getProperty(Config.PARSE_DOCS_NOW, "true"));
        if(!parse) {
            Trace.trace("Property " + Config.PARSE_DOCS_NOW + " is false. Parsing disabled.");
            return;
        }
        int resourceId = getLastReadIndex();
        while (docs.size() < required_docs_count) {
            Resource resource = resourcesRepository.get(++ resourceId);
            if(resource == null){
                Trace.trace("Resource is null. Stopping read");
                break;
            }
            PlainTextResource data = parser.parse(resource);
            boolean added = addData(data);
            onAdded(resourceId, added);
        }

    }

    private void onAdded(int resourceId, boolean added) throws IOException {
        if(added){
            Trace.trace("Doc " + resourceId + " parsed");
        } else {
            Trace.trace("Doc " + resourceId + " cannot be parsed");
        }
        writeLastReadIndex(resourceId);
    }

    protected boolean addData(PlainTextResource data) throws IOException {
        if(data == null){
            return false;
        }
        if(uniqueTitles.contains(data.getTittle())){
            Trace.trace("Document '" + data.getTittle() + "' is duplicated");
            return false;
        }
        docs.add(data);
        uniqueTitles.add(data.getTittle());
        return true;
    }

    private int getLastReadIndex() throws IOException {
        String indexPath = sourceDir + IOHelper.FILE_SEPARATOR + REPOSITORY_STATE_FILE;
        String indexStr = IOHelper.readFromFile(indexPath);
        return Integer.parseInt(indexStr.trim().replace("\n", ""));
    }

    private void writeLastReadIndex(int index) throws IOException {
        String indexPath = sourceDir + IOHelper.FILE_SEPARATOR + REPOSITORY_STATE_FILE;
        File f = new File(indexPath);
        if(!f.exists()){
            f.createNewFile();
        }
        IOHelper.saveToFile(indexPath, String.valueOf(index));
    }

}
