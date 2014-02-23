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

import java.util.Set;

/**
 * @author shredinger
 */
public class Word implements Comparable<Word> {
    private final String stemmedWord;
    private final Integer count;
    private final Set<String> originalWords;


    /**
     * @return the word
     */
    public String getStemmedWord() {
        return stemmedWord;
    }

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    public Set<String> getOriginalWords() {
        return originalWords;
    }

    /**
     *
     */
    public Word(String stemmedWord, Integer count, Set<String> originalWords) {
        this.stemmedWord = stemmedWord;
        this.count = count;
        this.originalWords = originalWords;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Word o) {
        if (this.count != o.count) {
            return o.count - this.count;
        }
        return this.stemmedWord.compareTo(o.stemmedWord);
    }

    @Override
    public String toString() {
        String originalWordsStr = originalWords.stream().reduce((x, y) -> x + ", " + y).get();
        return stemmedWord +  " " + count + "   [" + originalWordsStr + "]";
    }
}
