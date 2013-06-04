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
package infrascructure.data;

import infrascructure.data.parse.PlainDocsRepository;
import infrascructure.data.readers.ResourcesRepository;
import infrascructure.data.util.Trace;
import infrascructure.data.vocabulary.SimpleVocabularyBuider;
import infrascructure.data.vocabulary.Vocabulary;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author shredinger
 *
 */
public class OnlineLDALauncher {
    
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
	Trace.trace("starting ..");
	process();

    }

    
    private static void process() {	
	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
	
	final ResourcesRepository reader = context.getBean(ResourcesRepository.class);
	    try {		
		ExecutorService pool = Executors.newCachedThreadPool();
			
		final PlainDocsRepository docsRepo = context.getBean(PlainDocsRepository.class);
		pool.submit(new Runnable() {
		    
		    @Override
		    public void run() {
			try {
			    Trace.trace("reading docs ..");
			    reader.readAll();
			} catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			
		    }
		});
		
		
		Future<?> parseResult = pool.submit(new Runnable() {
		    
		    @Override
		    public void run() {
			try {
			    Trace.trace("parsing ..");
			    docsRepo.readAll();
			    Trace.trace("parsed ..");
			} catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			
		    }
		});	
		
		//TO DO: test!
		
		parseResult.get();
		Future<Vocabulary> vocabularyResult = pool.submit(new Callable<Vocabulary>() {
		   /* (non-Javadoc)
		     * @see java.util.concurrent.Callable#call()
		     */
		    @Override
		    public Vocabulary call() throws Exception {
			return SimpleVocabularyBuider.createInstance(docsRepo).buildVocabulary();		        
		    } 
		});
		Vocabulary vocabulary = vocabularyResult.get();
				
		//TO DO: launch ONLINE DATA for prepared data
		
	    } catch (Exception e) {
		e.printStackTrace();
	    }
    }
}
