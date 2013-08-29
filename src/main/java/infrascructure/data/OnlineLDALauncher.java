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
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;
import infrascructure.data.vocabulary.BaseVocabularyBuilder;
import infrascructure.data.vocabulary.Vocabulary;
import infrascructure.data.vocabulary.Word;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author shredinger
 */
public class OnlineLDALauncher {

    private Config config;

    /**
     * @param args
     */
    public static void main(String[] args) {
        Trace.trace("starting ..");
        String appContextPath = null;
        if(args.length > 0) {
            appContextPath = args[0];
        }
        new OnlineLDALauncher().process(appContextPath);

    }


    public void process(String appContextPath) {
        //AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);

        String testConfigPath = appContextPath == null ? "src/main/resources/custombeans.xml" : appContextPath;
        final ApplicationContext context = new FileSystemXmlApplicationContext(testConfigPath);

        final ResourcesRepository reader = context.getBean(ResourcesRepository.class);
        try {
            config = context.getBean(Config.class);

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
                        e.printStackTrace();
                    }

                }
            });

            parseResult.get();
            Future<Vocabulary> vocabularyResult = pool.submit(new Callable<Vocabulary>() {
                /* (non-Javadoc)
                  * @see java.util.concurrent.Callable#call()
                  */
                @Override
                public Vocabulary call() throws Exception {
                    Trace.trace("Building vocabulary ...");
                    return context.getBean(BaseVocabularyBuilder.class).buildVocabulary();
                }
            });
            Vocabulary vocabulary = vocabularyResult.get();
            saveVocabulary(vocabulary);
            Trace.trace("Building vocabulary - done");

            //TODO: to use in IntelligentSearch we need to notify all consumers

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveVocabulary(Vocabulary v) throws IOException {

        String vocabularyPath = config.getProperty(Config.VOCABULARY_PATH);
        String wordCountsPath = config.getProperty(Config.WORDCOUNTS_PATH);
        Set<String> words = v.getWords().keySet();
        Map<String, Integer> wordCounts = v.getWordCounts();

        //Replace with lambdas as soon as possible!
        Queue<Word> sortedWordCounts = new PriorityQueue<>();
        for (String word : wordCounts.keySet()) {
            Integer count = wordCounts.get(word);
            sortedWordCounts.add(new Word(word, count));
        }

        IOHelper.writeLinesToFile(vocabularyPath, words);
        try (PrintWriter pw = new PrintWriter(wordCountsPath)) {
            while (!sortedWordCounts.isEmpty()) {
                Word w = sortedWordCounts.poll();
                String line = w.getWord() + "\t" + w.getCount();
                pw.println(line);
            }
        }
    }


}
