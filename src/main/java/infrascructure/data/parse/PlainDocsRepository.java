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
import infrascructure.data.util.Trace;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author shredinger
 */
public class PlainDocsRepository extends CacheableReader<PlainTextResource> {


    private final int MAX_CACHE_SIZE = 100;

    private String sourceDir;
    private volatile BigList<PlainTextResource> docs;

    @Autowired
    private Parser parser;

    @Autowired
    private Config config;

    private int max_docs_count;

    private CacheableReader<Resource> resourcesRepository;

    /**
     *
     */
    public PlainDocsRepository(CacheableReader<Resource> resourcesRepository) {
        this.resourcesRepository = resourcesRepository;
    }

    @PostConstruct
    private void init() {
        max_docs_count = config.getPropertyInt(Config.REQUIRED_DOCS_COUNT);
        sourceDir = config.getProperty(Config.PLAINDOCS_DIR);
        String tittlesFileName = config.getProperty(Config.TITTLES_PATH);
        PlainTextResourceSerializer serializer = SerializersFactory.createPlainTextSerializer(sourceDir, tittlesFileName);
        docs = new SimpleCachedList<PlainTextResource>(sourceDir, MAX_CACHE_SIZE, serializer);
    }


    /* (non-Javadoc)
     * @see infrascructure.data.readers.CacheableReader#get(java.lang.Integer)
     */
    @Override
    public PlainTextResource get(Integer i) {
        return i >= max_docs_count ? null : docs.get(i);
    }

    /* (non-Javadoc)
     * @see infrascructure.data.readers.CacheableReader#readAll()
     */
    @Override
    public void readAll() throws IOException {
        int i = docs.size();
        while (i < max_docs_count) {
            Resource resource = resourcesRepository.get(i);
            PlainTextResource data = parser.parse(resource);
            if (data != null) {
                docs.add(data);
                Trace.trace("Doc " + i + " parsed");
            } else {
                Trace.trace("Doc " + i + " cannot be parsed");
            }
            i++;
        }

    }

}
