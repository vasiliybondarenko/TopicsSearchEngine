package vagueobjects.ir.lda.online;
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

import infrascructure.data.dom.Document;
import infrascructure.data.dom.DocumentImpl;
import vagueobjects.ir.lda.online.demo.DocumentData;
import vagueobjects.ir.lda.online.demo.OnlineLDAResult;
import vagueobjects.ir.lda.online.demo.Topic;
import vagueobjects.ir.lda.online.demo.WordTuple;
import vagueobjects.ir.lda.online.matrix.Matrix;
import vagueobjects.ir.lda.online.matrix.Vector;
import vagueobjects.ir.lda.tokens.OnlineLDASource;
import vagueobjects.ir.lda.tokens.Tuple;

import java.util.*;

/**
 * Displays topics discovered by Online LDA. Topics are sorted by
 * their statistical importance.
 */
public class Result implements OnlineLDAResult {
    private final Matrix lambda;
    private final Matrix gamma;
    private final double perplexity;
    private final OnlineLDASource documents;
    private final int totalTokenCount;

    /**
     * @param docs   - documents in the batch
     * @param D      - total number of documents in corpus
     * @param bound  - variational bound
     * @param lambda - variational distribution q(beta|lambda)
     * @param gamma
     */
    public Result(OnlineLDASource docs, int D, double bound, Matrix lambda, Matrix gamma) {
        this.lambda = lambda;
        this.gamma = gamma;
        this.documents = docs;
        this.totalTokenCount = docs.getTokenCount();
        double perWordBound = (bound * docs.size()) / D / totalTokenCount;
        this.perplexity = Math.exp(-perWordBound);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Perplexity estimate: ").append(perplexity).append("\n");
        int numTopics = lambda.getNumberOfRows();
        int numTerms = Math.min(NUMBER_OF_TOKENS, lambda.getNumberOfColumns());
        for (int k = 0; k < numTopics; ++k) {
            Vector termScores = lambda.getRow(k);

            for (Tuple tuple : sortTopicTerms(termScores, numTerms)) {
                tuple.addToString(sb, documents);
            }

            sb.append('\n');
        }
        sb.append("\n");
        return sb.toString();
    }

    public String getWordsTopicsDistribution() {
        StringBuilder sb = new StringBuilder();
        sb.append("Perplexity estimate: ").append(perplexity).append("\n");
        int numTopics = lambda.getNumberOfRows();
        int numTerms = Math.min(NUMBER_OF_TOKENS, lambda.getNumberOfColumns());
        for (int k = 0; k < numTopics; ++k) {
            Vector termScores = lambda.getRow(k);

            sb.append("[TOPIC " + k + "]: \n");
            for (Tuple tuple : sortTopicTerms(termScores, numTerms)) {
                tuple.addToString(sb, documents);
            }

            sb.append('\n');
        }
        sb.append("\n");
        return sb.toString();
    }

    public String getDocsDistribution(List<DocumentData> docs) {
        StringBuilder sb = new StringBuilder();
        int topics = gamma.getNumberOfColumns();
        int batchSize = gamma.getNumberOfRows();
        for (int d = 0; d < batchSize; d++) {
            int docId = docs.get(d).getId();
            String title = docs.get(d).getTitle();
            sb.append(docId).append(": ");
            sb.append(title).append(": ");
            Vector row = gamma.getRow(d);
            for (int k = 0; k < topics; k++) {
                sb.append(row.elementAt(k)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public List<Document> getDocuments(){
        int topics = gamma.getNumberOfColumns();
        int batchSize = gamma.getNumberOfRows();
        List<Document> resultDocuments = new ArrayList<>(batchSize);
        List<DocumentData> documentsData = documents.getDocumentsData();
        for (int d = 0; d < batchSize; d++) {
            int docId = documentsData.get(d).getId();
            String title = documentsData.get(d).getTitle();
            Vector row = gamma.getRow(d);
            double[] topicsDistribution = normalizeProbabilities(row);
            Document document = new DocumentImpl(docId, title, topicsDistribution);
            resultDocuments.add(document);
        }
        return resultDocuments;
    }

    @Override
    public List<Topic> getTopics(){
        int numTopics = lambda.getNumberOfRows();
        List<Topic> topics = new ArrayList<>(numTopics);
        for(int k = 0; k < numTopics; k ++){
            Vector termScores = lambda.getRow(k);
            int wordsCount = termScores.getLength();
            double[] termScoresNormalized = normalizeProbabilities(termScores);

            List<WordTuple> words = new ArrayList<>(wordsCount);
            for(int w = 0; w < wordsCount; w ++){
                String word = documents.getTokenById(w);
                words.add(new WordTuple(word, termScoresNormalized[w]));
            }
            Topic topic = new Topic(words);
            topics.add(topic);
        }

        return topics;
    }

    private Collection<Tuple> sortTopicTerms(Vector termScores, int numTerms) {
        Set<Tuple> tuples = new TreeSet<Tuple>();
        double[] p = normalizeProbabilities(termScores);

        for (int i = 0; i < termScores.getLength(); ++i) {
            Tuple tuple = new Tuple(i, p[i]);
            tuples.add(tuple);
        }
        return new ArrayList<Tuple>(tuples).subList(0, numTerms);
    }

    private double[] normalizeProbabilities(Vector termScores) {
        double sum = 0d;
        for (int i = 0; i < termScores.getLength(); ++i) {
            sum += termScores.elementAt(i);
        }

        double[] p = new double[termScores.getLength()];
        for (int i = 0; i < termScores.getLength(); ++i) {
            p[i] = termScores.elementAt(i) / sum;
        }
        return p;
    }

}
