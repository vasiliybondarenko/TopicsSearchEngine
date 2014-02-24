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
package infrascructure.data.vocabulary;

import infrascructure.data.Config;
import infrascructure.data.dom.ResourceMetaData;
import infrascructure.data.readers.CacheableReader;
import infrascructure.data.stripping.Stemmer;
import infrascructure.data.util.IOHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author shredinger
 */
public class SimpleVocabularyBuider extends BaseVocabularyBuilder {

    protected final static int LOWER_BOUND = 1;

    @Autowired
    private Config config;

    @Autowired
    private Stemmer stemmer;

    protected CacheableReader<ResourceMetaData> reader;
    protected int min_count;
    protected int from_doc;
    protected int to_doc;
    protected List<String> words;
    private Set<String> stopWords;

    public Vocabulary buildVocabulary() {
        return createVocabulary();


        //FOR TEST!
//	try {
//	   
//	    Collections.sort(words);
//	    IOHelper.writeLinesToFile(Config.getProperty("vocabulary_path"), words);
//	    
//	    Trace.trace("Vocabulary has been created successfully");
//	} catch (IOException e) {
//	    // TODO Auto-generated catch block
//	    e.printStackTrace();
//	}
    }

    /**
     *
     */
    public SimpleVocabularyBuider(CacheableReader<ResourceMetaData> reader, int min_count) {
        this.reader = reader;
        this.min_count = min_count;
        this.from_doc = 0;
    }

    @PostConstruct
    private void init() {
        this.to_doc = config.getPropertyInt(Config.REQUIRED_DOCS_COUNT) - 1;
        this.stopWords = getStopWords();
    }

    protected Vocabulary createVocabulary() {
        Map<String, Word> allWords = new HashMap<>();
        for (int i = from_doc; i <= to_doc; i++) {
            Collection<Word> words = retrieveAllWordCounts(i);
            for (Word word : words) {
                String stemmedWord = word.getStemmedWord();
                if(allWords.containsKey(stemmedWord)){
                    Word existedWord = allWords.get(stemmedWord);
                    existedWord.getOriginalWords().addAll(word.getOriginalWords());
                    allWords.put(stemmedWord, new Word(stemmedWord, existedWord.getCount() + word.getCount(), existedWord.getOriginalWords()));
                }else{
                    allWords.put(stemmedWord, word);
                }
            }
        }
        Map<String, Word> wordCounts = new HashMap<>();
        Map<String, Integer> wordIds = new HashMap<>();
        int id = 0;
        int max = config.getPropertyInt(Config.REQUIRED_DOCS_COUNT) / config.getPropertyInt(Config.TOPICS);
        for (String word : allWords.keySet()) {
            int docsCount = allWords.get(word).getCount();
            if (docsCount >= min_count && docsCount <= max) {
                wordCounts.put(word, allWords.get(word));
                wordIds.put(word, id++);
            }
        }
        Vocabulary vocabulary = new VocabularyImpl(wordIds, new ArrayList<>(wordCounts.values()));
        return vocabulary;
    }


    private Set<String> getStopWords() {
        String stopWordsPath = config.getProperty(Config.STOP_WORDS_PATH);
        try {
            List<String> words = IOHelper.readLinesFromFile(stopWordsPath);
            Set<String> stopWords = new HashSet<String>();
            for (String word : words) {
                stopWords.add(word);
            }
            return stopWords;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<String>();
    }

    protected Collection<Word> retrieveAllWordCounts(int i) {
        ResourceMetaData r = reader.get(i);
        if(r != null){
            return retrieveWords(r);
        }
        return new ArrayList<>(1);
    }


    protected Collection<Word> retrieveWords(ResourceMetaData resource) {
        String wordPattern = WORD_PATTERN;
        Pattern pattern = Pattern.compile(wordPattern, Pattern.CASE_INSENSITIVE);
        String source = resource.getData();
        Matcher matcher = pattern.matcher(source);

        HashMap<String, Word> wordsMap = new LinkedHashMap<>(200);
        while (matcher.find()) {
            String word = matcher.group().toLowerCase();

            int indexOfAp = word.indexOf("'");
            if(indexOfAp != -1){
                String[] ww = word.split("'");
                word = ww[0];
            }

            if (!stopWords.contains(word)) {
                String stemmedWord = stemmer.getCanonicalForm(word);
                if(!stopWords.contains(stemmedWord) && word.length() > 2){
                    Set<String> originalWords = wordsMap.containsKey(stemmedWord) ? wordsMap.get(stemmedWord).getOriginalWords() : new HashSet<>();
                    originalWords.add(word);
                    if(!wordsMap.containsKey(stemmedWord)){
                        wordsMap.put(stemmedWord, new Word(stemmedWord, 1, originalWords));
                    }else{
                        Word existedToken = wordsMap.get(stemmedWord);
                        wordsMap.put(stemmedWord, new Word(stemmedWord, existedToken.getCount() + 1, originalWords));
                    }
                }
            }
        }

        return wordsMap.values();
    }


}
