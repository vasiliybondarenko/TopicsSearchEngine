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


import infrascructure.data.util.CloseableWriter;
import infrascructure.data.util.DefaultFileWriter;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;
import vagueobjects.ir.lda.online.Config;
import vagueobjects.ir.lda.online.Result;
import vagueobjects.ir.lda.online.TopicModelAlgorithm;
import vagueobjects.ir.lda.tokens.Documents;
import vagueobjects.ir.lda.tokens.QuickVocabulary;
import vagueobjects.ir.lda.tokens.Vocabulary;

import java.io.IOException;
import java.util.List;

/**
 * @author shredinger
 */
public class StandaloneOnlineLDAExecutor {
    static final String defaultPropertiesPath = "/Users/shredinger/Documents/DEVELOPMENT/Projects/SHARED/IntelligentSearch/src/main/resources/localSampleOnlinelda.properties";

    static  int K;
    static int batchSize;




    public static void main(String[] args) {
        try {
            String path = args.length > 0 ? args[0] : defaultPropertiesPath;
            Config.load(path);

            K = Integer.parseInt(Config.getProperty("topics"));
            batchSize = Integer.parseInt(Config.getProperty("batch_size", "1024"));

            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     */
    public static void start() throws Exception {
        Trace.trace("Loading ...");
        try (BatchesReader batchesReader = new FileSystemBatchesReader()){
            Vocabulary vocabulary = new QuickVocabulary(getCurrentVocabulary());
            DefaultTopicsModelAlgorithmFactory algorithmFactory = new DefaultTopicsModelAlgorithmFactory();
            doOnlineLDA(algorithmFactory, batchesReader, vocabulary);
        }

    }

    private static void doOnlineLDA(TopicsModelAlgorithmFactory algorithmFactory, BatchesReader batchesReader, Vocabulary vocabulary) throws IOException {
        List<DocumentData> docs;

        Trace.trace("Online LDA initializing ...");
        Trace.trace("Topics: " + K);
        int vocabularySize = vocabulary.size();
        Trace.trace("Vocabulary size: " + vocabularySize);
        Trace.trace("Batch size: " + batchSize);

        TopicModelAlgorithm lda = algorithmFactory.createTopicModel(vocabularySize, K);
        int batch = 0;
        do {
            Trace.trace("==================== Batch " + batch++ + " ========================");
            Trace.trace("Reading docs ...");
            docs = batchesReader.getNextBatch(batchSize);
            if (docs != null) {
                Trace.trace("Read " + docs.size() + " docs");
                Documents documents = new Documents(docs, vocabulary);
                Trace.trace("OnlineLDA os starting ...");
                long startTime = System.nanoTime();

                Result result = lda.workOn(documents);

                onInferenceCompleted(batch, result);
                Trace.trace("Writing results... Batch execution time: " + (System.nanoTime() - startTime) / 1000000000 + "sec");
            } else {
                Trace.trace("Stopped. No docs available");
            }

        } while (docs != null);
    }

    private static void onInferenceCompleted(int batch, Result result) throws IOException {
        String docsDistributionPath = String.format("%s_batch=%d.txt", Config.getProperty("onlinelds.results.docs"), batch);
        String topWordsPath = Config.getProperty("onlinelds.results");

        OnlineLDAResultWriter ldaResultWriter = new DefaultOnlineLDAResultWriter();

        try(CloseableWriter topWordsWriter = new DefaultFileWriter(topWordsPath)){
            ldaResultWriter.writeTopWords(result, topWordsWriter, OnlineLDAResult.NUMBER_OF_TOKENS);
        }
        try (DefaultFileWriter docWriter = new DefaultFileWriter(docsDistributionPath)){
            ldaResultWriter.writeDocumentTopicsDistribution(result, docWriter);
        }
    }

    public static List<String> getCurrentVocabulary() throws IOException {
        long startTime = System.nanoTime();
        String path = Config.getProperty("vocabulary_path");
        List<String> result = IOHelper.readLinesFromFile(path);
        long diff = System.nanoTime() - startTime;
        Trace.trace("[getCurrentVocabulary]: " + diff);
        return result;
    }



}
