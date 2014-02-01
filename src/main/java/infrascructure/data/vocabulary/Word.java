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

/**
 * @author shredinger
 */
public class Word implements Comparable<Word> {
    private final String word;
    private final Integer count;

    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     *
     */
    public Word(String word, Integer count) {
        this.word = word;
        this.count = count;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Word o) {
        if (this.count != o.count) {
            return o.count - this.count;
        }
        return this.word.compareTo(o.word);
    }

    @Override
    public String toString() {
        return word + " " + count;
    }
}
