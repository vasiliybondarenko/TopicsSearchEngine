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
import infrascructure.data.PlainTextResource;
import infrascructure.data.readers.CacheableReader;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author shredinger
 *
 */
public class SimpleVocabularyBuider extends BaseVocabularyBuilder{    
    
    protected final static int LOWER_BOUND = 1;
    protected final static int REQUIRED_DOCS_COUNT = Integer.parseInt(Config.getProperty("required_docs_count"));
    
    protected CacheableReader<PlainTextResource> reader;    
    protected int min_count; 
    protected int from_doc;
    protected int to_doc;
    protected List<String> words;
    
    public static SimpleVocabularyBuider createInstance(CacheableReader<PlainTextResource> reader) {
	int min = Integer.parseInt(Config.getProperty("min_docs_count", String.valueOf(LOWER_BOUND)));
	return new SimpleVocabularyBuider(reader, min);
    }
    
    public Vocabulary buildVocabulary() {
	words = createVocabulary();
	//TO DO: init vocabulary instance
	
	//FOR TEST!
	try {
	   
	    Collections.sort(words);
	    IOHelper.writeLinesToFile(Config.getProperty("vocabulary_path"), words);
	    
	    Trace.trace("Vocabulary has been created successfully");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	return null;
    }
    
    /**
     * 
     */
    protected SimpleVocabularyBuider(CacheableReader<PlainTextResource> reader, int min_count) {
	this.reader = reader;
	this.min_count = min_count;
	this.from_doc = 0;
	this.to_doc = REQUIRED_DOCS_COUNT - 1;
    }
    
    protected List<String> createVocabulary(){
	//TO DO: CHANGE: we need to get docs count for particular word!
	Set<String> stopWords = getStopWords();
	
	List<String> vocabulary = new LinkedList<String>();
	Map<String, Integer> allWords = new HashMap<String, Integer>();	
	for(int i = from_doc; i <= to_doc; i ++) {	    
	    Map<String, Integer> words = retrieveAllWordCounts(i);	    
	    for(String word: words.keySet()) {
		 int docsCount = allWords.containsKey(word) ? allWords.get(word) : 0;
		 allWords.put(word, docsCount + 1);
	    }
	}	
	int max = REQUIRED_DOCS_COUNT / 3;
	for(String word: allWords.keySet()) {
	    int docsCount = allWords.get(word);
	    if(!stopWords.contains(word) && docsCount >= min_count && docsCount <= max) {
		vocabulary.add(word);
	    }
	}
	return vocabulary;
    }

    
    private Set<String> getStopWords() {	
	String stopWordsPath = Config.getProperty("stop_words_path");
	try {
	    List<String> words = IOHelper.readLinesFromoFile(stopWordsPath);
	    Set<String> stopWords = new HashSet<String>();
	    for(String word: words) {
		stopWords.add(word);
	    }
	    return stopWords;
	} catch (IOException e) {	    
	    e.printStackTrace();
	}
	return new HashSet<String>();
    }  
    
    protected Map<String, Integer> retrieveAllWordCounts(int i) {
	Map<String, Integer> allWordCounts = new HashMap<String, Integer>();	
	PlainTextResource r = reader.get(i);
	    Set<String> wordCounts = retrieveWordCounts(r);
	    for(String word: wordCounts) {
		Integer count = allWordCounts.containsKey(word) ? allWordCounts.get(word) : 0;
		allWordCounts.put(word, count + 1);
	    }
	return allWordCounts;
    }  

    
    protected Set<String> retrieveWordCounts(PlainTextResource resource) {
	String wordPattern = "[a-zA-Z]+";
        Pattern pattern = Pattern.compile(wordPattern,  Pattern.CASE_INSENSITIVE);
        String source = resource.getText();
        Matcher matcher = pattern.matcher(source);
        Set<String> tokens = new HashSet<String>();
	while(matcher.find()) {
	    String word = matcher.group().toLowerCase();	    
	    if(!tokens.contains(word)) {
		tokens.add(word);
	    }	    
	}
	
	return tokens;
    }    
   
}
