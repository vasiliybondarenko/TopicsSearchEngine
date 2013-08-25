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


import infrascructure.data.launch.DocsRepository;
import infrascructure.data.launch.DocsRepositoryFactory;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import vagueobjects.ir.lda.online.Config;
import vagueobjects.ir.lda.online.OnlineLDA;
import vagueobjects.ir.lda.online.Result;
import vagueobjects.ir.lda.tokens.Documents;
import vagueobjects.ir.lda.tokens.QuickVocabulary;
import vagueobjects.ir.lda.tokens.Vocabulary;

/**
 * @author shredinger
 *
 */
public class OnlineLDAExecutor {

    public static void main(String[] args) {
	try {
	    start();
	} catch (Exception e) {	 
	    e.printStackTrace();
	}
    }
    
    /**
     */
    public static void start() throws Exception{

        int D=  10800;
        int K = 100;
        int batchSize= Integer.parseInt(Config.getProperty("batch_size", "1024"));

        double tau =  1d;
        double kappa =  0.8d;

        double alpha = 1.d/K;
        double eta = 1.d/K;

        Trace.trace("Loading ...");
        
        DocsRepository docsRepository = DocsRepositoryFactory.getDocsRepository();       
        Vocabulary vocabulary = new QuickVocabulary(docsRepository.getCurrentVocabulary());
        List<String> docs;
        
        Trace.trace("Online LDA initializing ...");
        
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
                Result result = lda.workOn(documents);
                String data = result.getWordsTopicsDistribution();
                IOHelper.saveToFile(Config.getProperty("onlinelds.results"), data);                
            }else {
        	Trace.trace("Stopped. No docs available");
            }
            
        }while(docs != null);       
       
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
