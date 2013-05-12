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
import infrascructure.data.vocabulary.VocabularyBuilder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author shredinger
 *
 */
public class OnlineLDALauncher {
    
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
	
	process();

    }

    
    private static void process() {
	//TO DO: get instance of reader from annotation context
	final ResourcesRepository reader = new ResourcesRepository();
	    try {
		
		//example
		ExecutorService pool = Executors.newCachedThreadPool();
		final PlainDocsRepository docsRepo = new PlainDocsRepository(reader);
		Future future = pool.submit(new Runnable() {
		    
		    @Override
		    public void run() {
			try {
			    reader.readAll();
			} catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			
		    }
		});
		
		Future future1 = pool.submit(new Runnable() {
		    
		    @Override
		    public void run() {
			try {
			    docsRepo.readAll();
			} catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			
		    }
		});
		
		
		
		//TO DO: run in separated threads using Executers
//		reader.readAll();		
//		PlainDocsRepository docsRepo = new PlainDocsRepository(reader);
//		docsRepo.readAll();
		
		Vocabulary vocabulary = SimpleVocabularyBuider.createInstance(docsRepo).buildVocabulary();		
		//TO DO: launch ONLINE DATA for prepared data
		
	    } catch (Exception e) {
		e.printStackTrace();
	    }
    }
}
