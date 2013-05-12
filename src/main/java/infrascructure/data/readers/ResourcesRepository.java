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

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import infrascructure.data.Config;
import infrascructure.data.Resource;
import infrascructure.data.crawl.LocalIteratorFactory;
import infrascructure.data.crawl.URLIterator;
import infrascructure.data.list.BigList;
import infrascructure.data.serialize.SerializersFactory;
import infrascructure.data.serialize.SimpleResourceSerializer;

/**
 * @author shredinger
 *
 */
public class ResourcesRepository extends CacheableReader<Resource>{

    private final int MAX_CACHE_SIZE = 100;
    private final int MAX_DOCS_COUNT = Integer.parseInt(Config.getProperty("required_docs_count"));;
    
    @Autowired
    private URLIterator urlIterator;
    
    @Autowired
    private ResourceReader reader;
    
    private String sourceDir;
    private volatile BigList<Resource> rawdocs;
    
    /**
     * 
     */
    public ResourcesRepository() {	
	sourceDir = Config.getProperty("rawdocs_repository");	
	SimpleResourceSerializer serializer = SerializersFactory.createSimpleSerializer(sourceDir);
	rawdocs = new SimpleCachedList<Resource>(sourceDir, MAX_CACHE_SIZE, serializer);
    }
    
    /**
     * @return the rawdocs
     */
    @Override
    public Resource get(Integer i) {
	return i >= MAX_DOCS_COUNT ? null : rawdocs.get(i);
    }
    
    @Override
    public void readAll() throws IOException {	
	String url;
	int i = rawdocs.size();
	while(i++ < MAX_DOCS_COUNT){	    
	    url = urlIterator.getNextURL();
	    if(url == null) {
		break;
	    }
	    Resource resourse = reader.read(url);
	    rawdocs.add(resourse);	    
	}
    }
}
