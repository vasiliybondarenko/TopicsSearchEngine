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
import infrascructure.data.dom.ResourceMetaData;
import infrascructure.data.dom.Tag;
import infrascructure.data.dom.Tags;
import infrascructure.data.list.BigList;
import infrascructure.data.readers.CacheableReader;
import infrascructure.data.util.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashSet;

/**
 * @author shredinger
 */
public class PlainDocsRepository extends CacheableReader<ResourceMetaData> {


    private final int MAX_CACHE_SIZE = 1;
    private final String REPOSITORY_STATE_FILE = "last_index.txt";

    private String sourceDir;

    @Autowired
    @Qualifier(value = "plainDocsList")
    private volatile BigList<ResourceMetaData> docs;

    @Autowired
    private Parser parser;

    @Autowired
    private Config config;

    private HashSet<String> uniqueTitles;

    private int required_docs_count;

    private CacheableReader<ResourceMetaData> resourcesRepository;

    /**
     *
     */
    public PlainDocsRepository(CacheableReader<ResourceMetaData> resourcesRepository) {
        this.resourcesRepository = resourcesRepository;
    }

    @PostConstruct
    private void init() throws IOException {
        required_docs_count = config.getPropertyInt(Config.REQUIRED_DOCS_COUNT);
        sourceDir = config.getProperty(Config.PLAINDOCS_DIR);
    }

    /* (non-Javadoc)
     * @see infrascructure.data.readers.CacheableReader#get(java.lang.Integer)
     */
    @Override
    public ResourceMetaData get(Integer i) {
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
        int resourceId = docs.size();
        while (docs.size() < required_docs_count) {
            ResourceMetaData resource = resourcesRepository.get(resourceId ++);
            if(resource == null){
                Trace.trace("Resource is null. Trying next ...");
                continue;
            }
            PlainTextResource data = parser.parse(resource);
            Tag[] tags = new Tag[]{
                 resource.getTag(Tags.URL),
                 new Tag(Tags.TITLE, data.getTittle())
            };
            ResourceMetaData resourceMetaData = new ResourceMetaData(resourceId, data.getData(), tags);

            boolean added = addData(resourceMetaData);
            onAdded(resourceId, added);
        }

    }

    private void onAdded(int resourceId, boolean added) throws IOException {
        if(added){
            Trace.trace("Doc " + resourceId + " parsed");
        } else {
            Trace.trace("Doc " + resourceId + " cannot be parsed");
        }
    }

    protected boolean addData(ResourceMetaData data) throws IOException {
        if(data == null){
            return false;
        }
        docs.add(data);
        return true;
    }

}
