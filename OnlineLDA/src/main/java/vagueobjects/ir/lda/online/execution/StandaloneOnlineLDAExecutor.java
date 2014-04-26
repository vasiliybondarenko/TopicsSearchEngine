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


import infrascructure.data.stripping.StemmerFactory;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;
import vagueobjects.ir.lda.online.Config;
import vagueobjects.ir.lda.online.demo.FilesBatchesReadersFactory;

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

            BaseExecutor executor = new LocalOnlineLDAExecutor(StemmerFactory.createStemmer(), topics, batchSize, new FilesBatchesReadersFactory(batchSize));
            executor.start();

        } catch (Exception e) {
            e.printStackTrace();
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
