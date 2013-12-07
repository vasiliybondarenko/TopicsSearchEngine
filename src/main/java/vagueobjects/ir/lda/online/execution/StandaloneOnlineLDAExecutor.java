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
package vagueobjects.ir.lda.online.execution;


import infrascructure.data.util.CloseableWriter;
import infrascructure.data.util.DefaultFileWriter;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;
import vagueobjects.ir.lda.online.Config;
import vagueobjects.ir.lda.online.Result;
import vagueobjects.ir.lda.online.TopicModelAlgorithm;
import vagueobjects.ir.lda.online.demo.*;
import vagueobjects.ir.lda.tokens.OnlineLDASource;
import vagueobjects.ir.lda.tokens.Vocabulary;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * @author shredinger
 */
public class StandaloneOnlineLDAExecutor {
    static final String defaultPropertiesPath = "/Users/shredinger/Documents/DEVELOPMENT/Projects/SHARED/IntelligentSearch/src/main/resources/localSampleOnlinelda.properties";

    public static void main(String[] args) {
        try {
            String path = args.length > 0 ? args[0] : defaultPropertiesPath;
            Config.load(path);

            int topics = Integer.parseInt(Config.getProperty("topics"));
            int batchSize = Integer.parseInt(Config.getProperty("batch_size", "1024"));
            BaseExecutor executor = new Executor(topics, batchSize, new FilesBatchesReadersFactory());
            executor.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class Executor extends BaseExecutor{

        public Executor(int topics, int batchSize, BatchesReadersFactory batchesReadersFactory) {
            super(topics, batchSize, batchesReadersFactory);
        }

        @Override
        protected void processBatches(TopicsModelAlgorithmFactory algorithmFactory, BatchesReader batchesReader, Vocabulary vocabulary) throws IOException {
            Trace.trace("Online LDA initializing ...");
            Trace.trace("Topics: " + topics);
            Trace.trace("Vocabulary size: " + vocabulary.size());
            Trace.trace("Batch size: " + batchSize);
            List<DocumentData> docs;
            TopicModelAlgorithm lda = algorithmFactory.createTopicModel(vocabulary.size(), topics);
            int batch = 0;
            while((docs = batchesReader.getNextBatch(batchSize)) != null) {
                batch ++;
                processSingleBatch(vocabulary, docs, lda, batch);
            }
            postProcess();
        }

        @Override
        protected void processSingleBatch(Vocabulary vocabulary, List<DocumentData> docs, TopicModelAlgorithm lda, int batch) throws IOException {
            Trace.trace("==================== Batch " + batch++ + " ========================");
            Trace.trace("Read " + docs.size() + " docs");
            OnlineLDASource documents = OnlineLDASource.createDocuments(docs, vocabulary);
            Trace.trace("OnlineLDA is starting ...");

            ExecutionResult<Result> executionResult = Exec.execute(lda::workOn, documents);
            OnlineLDAResult result = executionResult.getResult();
            long executionTime = executionResult.getExecutionTime();

            Trace.trace("Writing results... Batch execution time: " + (executionTime / 1000000000) + "sec");
            postProcessBatch(batch, result);
        }

        @Override
        protected void preProcess() {
            Trace.trace("Loading ...");
        }

        @Override
        protected void postProcess() {
            Trace.trace("Stopped. No docs available");
        }

        @Override
        protected void postProcessBatch(int batch, OnlineLDAResult result) throws IOException {
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
    }

    public static List<String> getCurrentVocabulary() throws IOException {
        long startTime = System.nanoTime();
        String path = Config.getProperty("vocabulary_path");
        List<String> result = IOHelper.readLinesFromFile(path);
        long diff = System.nanoTime() - startTime;
        Trace.trace("[getCurrentVocabulary]: " + diff);
        return result;
    }



    public static class Exec{
        public static <T, R> ExecutionResult<R> execute(Function<T, R> f, T source){
            long start = System.nanoTime();
            R result = f.apply(source);
            long time = System.nanoTime() - start;
            return new ExecutionResult<R>(time, result);
        }
    }

    public static class ExecutionResult<Result>{
        private final long time;
        private final Result result;


        public ExecutionResult(long time, Result result) {
            this.time = time;
            this.result = result;
        }

        public long getExecutionTime() {
            return time;
        }

        public Result getResult() {
            return result;
        }
    }

}
