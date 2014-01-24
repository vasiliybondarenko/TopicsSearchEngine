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
package vagueobjects.ir.lda.tokens;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

/**
 * @author shredinger
 */
public class QuickVocabulary implements Vocabulary {

    private final BiMap<String, Integer> words = HashBiMap.create();

    public QuickVocabulary(Collection<String> strings) {
        int i = 0;
        for (String word : strings) {
            words.put(word, i++);
        }
    }

    public QuickVocabulary(String path) throws IOException {
        try (Scanner scanner = new Scanner(new File(path))) {
            int i = 0;
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim();
                words.put(word, i++);
            }
        }
    }

    /* (non-Javadoc)
     * @see vagueobjects.ir.lda.tokens.Vocabulary#contains(java.lang.String)
     */
    @Override
    public boolean contains(String token) {
        return words.containsKey(token);
    }

    /* (non-Javadoc)
     * @see vagueobjects.ir.lda.tokens.Vocabulary#size()
     */
    @Override
    public int size() {
        return words.size();
    }

    /* (non-Javadoc)
     * @see vagueobjects.ir.lda.tokens.Vocabulary#getId(java.lang.String)
     */
    @Override
    public int getId(String token) {
        return words.get(token);
    }

    /* (non-Javadoc)
     * @see vagueobjects.ir.lda.tokens.Vocabulary#getTokenById(int)
     */
    @Override
    public String getToken(int id) {
        return words.inverse().get(id);
    }

}
