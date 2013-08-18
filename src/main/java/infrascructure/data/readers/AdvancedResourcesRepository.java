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
import infrascructure.data.util.Trace;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shredinger
 *
 */

public class AdvancedResourcesRepository extends ResourcesRepository implements Runnable{

    public final static int THREADS = Integer.parseInt(Config.getProperty("crawl_threads", "2"));
    private volatile AtomicInteger index;
    
    /* (non-Javadoc)
     * @see infrascructure.data.readers.ResourcesRepository#readAll()
     */
    @Override
    public void readAll() throws IOException {
	int i = rawdocs.size();
	index = new AtomicInteger(i);
	
	ExecutorService pool = Executors.newCachedThreadPool();
	for(int t = 0; t < THREADS; t ++) {
	    pool.submit(this);
	}	
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
	String url;	
	while(index.get() < MAX_DOCS_COUNT){
	    url = urlIterator.getNextURL();
	    if(url == null) {
		break;
	    }	    	    
	    Resource resource = reader.read(url);
	    if(resource == null) {
		Trace.trace("Skipping resource " + index + " ...");
		continue;
	    }
	    try {
		rawdocs.add(resource);
		Trace.trace("Doc " + index + " was read");
	    } catch (IOException e) {		
		e.printStackTrace();
	    }
        index.incrementAndGet();
	    
	}        
    }
    

   
}
