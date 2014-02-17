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
import infrascructure.data.dao.ResultLinkDao;
import infrascructure.data.dom.ResourceMetaData;
import infrascructure.data.dom.Tag;
import infrascructure.data.dom.Tags;
import infrascructure.data.email.html.entity.ResultLink;
import infrascructure.data.util.Trace;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shredinger
 */

public class AdvancedResourcesRepository extends ResourcesRepository implements Runnable {

    private volatile AtomicInteger index;

    @Autowired
    private ResultLinkDao resultLinkDao;

    /* (non-Javadoc)
     * @see infrascructure.data.readers.ResourcesRepository#readAll()
     */
    @Override
    public void readAll() throws IOException {
        int threads = config.getPropertyInt(Config.CRAWL_THREADS, 1);
        int i = rawdocs.size();
        index = new AtomicInteger(i);

        ExecutorService pool = Executors.newCachedThreadPool();
        for (int t = 0; t < threads; t++) {
            pool.submit(this);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        String url;
        while (index.get() < max_docs_count) {
            url = urlIterator.getNextURL();
            if (url == null) {
                break;
            }
            Resource resource = reader.read(url);
            if (resource == null) {
                Trace.trace("Skipping resource " + index + " ...");
                continue;
            }
            if(resource.getIdentifier() == null){
                resource = new Resource(resource.getData(), String.valueOf(index.get()));
            }

            Integer id = index.get();
            Tag[] tags = new Tag[]{
                    new Tag(Tags.URL, url)
            };
            ResourceMetaData resourceMetaData = new ResourceMetaData(id, resource.getData(), tags);
            try {
                rawdocs.add(resourceMetaData);
                Trace.trace("Doc " + index + " was read");
            } catch (IOException e) {
                e.printStackTrace();
            }
            index.incrementAndGet();

        }
    }

    protected void updateResultLin(String url, Resource resource) {
        ResultLink link = resultLinkDao.findByUrl(url);
        link.setRawDocId(resource.getIdentifier());
        resultLinkDao.save(link);
    }


}
