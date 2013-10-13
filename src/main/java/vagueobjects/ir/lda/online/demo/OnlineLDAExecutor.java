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
package vagueobjects.ir.lda.online.demo;


import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;
import org.apache.commons.io.IOUtils;
import vagueobjects.ir.lda.online.Config;
import vagueobjects.ir.lda.online.OnlineLDA;
import vagueobjects.ir.lda.online.Result;
import vagueobjects.ir.lda.tokens.Document;
import vagueobjects.ir.lda.tokens.Documents;
import vagueobjects.ir.lda.tokens.QuickVocabulary;
import vagueobjects.ir.lda.tokens.Vocabulary;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shredinger
 *
 */
public class OnlineLDAExecutor {

    public static void main(String[] args) {
	try {
        String path = args.length > 0 ? args[0] : "/Users/shredinger/Documents/DEVELOPMENT/Projects/SHARED/OnlineLDA/jolda/onlinelda.properties";
        Config.load(path);
	    start();
	} catch (Exception e) {	 
	    e.printStackTrace();
	}
    }
    
    /**
     */
    public static void start() throws Exception{

        int D=  10800;
        int K = Integer.parseInt(Config.getProperty("topics"));
        int batchSize= Integer.parseInt(Config.getProperty("batch_size", "1024"));

        double tau =  1d;
        double kappa =  0.8d;

        double alpha = 1.d/K;
        double eta = 1.d/K;

        Trace.trace("Loading ...");
        
        SimpleDocsRepository docsRepository = new SimpleDocsRepository();
        try {	    
	    Vocabulary vocabulary = new QuickVocabulary(docsRepository.getCurrentVocabulary());
	    List<Document> docs;
	    
	    Trace.trace("Online LDA initializing ...");
	    Trace.trace("Topics: " + K);
	    Trace.trace("Vocabulary size: " + vocabulary.size());
	    Trace.trace("Batch size: " + batchSize);
	    
	    OnlineLDA lda = new OnlineLDA(vocabulary.size(),K, D, alpha, eta, tau, kappa);
	    int batch = 0;
	    do {
	        Trace.trace("==================== Batch " + batch ++ + " ========================");
	        Trace.trace("Reading docs ...");
	        docs = docsRepository.getBatchDocs(batchSize);
	        if(docs != null) {
        	    	Trace.trace("Read " + docs.size() + " docs");
        	    	Documents documents = new Documents(docs, vocabulary);
        	    	Trace.trace("OnlineLDA os starting ...");
        	    	long startTime = System.nanoTime();
        	        Result result = lda.workOn(documents);
        	            
        	        String topWords = result.getWordsTopicsDistribution();
        	        String docsDistribution = result.getDocsDistribution(docs);
        	        Trace.trace("Writing results... Batch execution time: " + (System.nanoTime() - startTime) / 1000000000 + "sec");
        	        IOHelper.saveToFile(Config.getProperty("onlinelds.results"), topWords);
        	        IOHelper.saveToFile(Config.getProperty("onlinelds.results.docs") + "_batch=" + batch + ".txt", docsDistribution);	                            
	        }else {
	    	    Trace.trace("Stopped. No docs available");
	        }
	        
	    }while(docs != null);
	} finally{
	    docsRepository.close();
	}       
       
    }
 
    static List<String> readDocs(String path) throws IOException{

        List<String> strings = new ArrayList<String>();
        File dir = new File(path);
        for(File f :  dir.listFiles()){
            InputStream is = new FileInputStream(f);
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer);
            strings.add(writer.toString());
            is.close();

        }
        return strings;
    }

}
