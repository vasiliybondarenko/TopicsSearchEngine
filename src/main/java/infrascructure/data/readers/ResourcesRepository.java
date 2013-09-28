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
package infrascructure.data.readers;

import infrascructure.data.Config;
import infrascructure.data.Resource;
import infrascructure.data.crawl.URLIterator;
import infrascructure.data.list.BigList;
import infrascructure.data.serialize.SerializersFactory;
import infrascructure.data.serialize.SimpleResourceSerializer;
import infrascructure.data.util.Trace;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author shredinger
 */
public class ResourcesRepository extends CacheableReader<Resource> {

    private final int MAX_CACHE_SIZE = 100;
    @Autowired
    protected Config config;

    @Autowired
    protected URLIterator urlIterator;

    @Autowired
    protected ResourceReader reader;

    protected String sourceDir;
    protected int max_docs_count;
    protected BigList<Resource> rawdocs;

    /**
     *
     */
    public ResourcesRepository() {
    }

    @PostConstruct
    private void init(){
        max_docs_count = config.getPropertyInt(Config.MAX_DOCS_COUNT);
        sourceDir = config.getProperty(Config.RAWDOCS_REPOSITORY);
        SimpleResourceSerializer serializer = SerializersFactory.createSimpleSerializer(sourceDir);
        rawdocs = new SimpleCachedList<Resource>(sourceDir, MAX_CACHE_SIZE, serializer);
    }

    /**
     * @return the Resource object or null if index is out of range
     */
    @Override
    public Resource get(Integer i) {
        return i >= max_docs_count ? null : rawdocs.get(i);
    }

    @Override
    public void readAll() throws IOException {
        String url;
        int i = rawdocs.size();
        while (i++ < max_docs_count) {
            url = urlIterator.getNextURL();
            if (url == null) {
                break;
            }
            Resource resource = reader.read(url);
            if (resource == null) {
                Trace.trace("Skipping resource ...");
                continue;
            }
            rawdocs.add(resource);
            Trace.trace("Doc " + i + " was read");
        }
    }
}
