package vagueobjects.ir.lda.online.execution;

import infrascructure.data.InferenceTaskExecutor;
import infrascructure.data.stripping.Stemmer;
import vagueobjects.ir.lda.online.TopicModelAlgorithm;
import vagueobjects.ir.lda.online.demo.*;
import vagueobjects.ir.lda.tokens.OnlineLDASource;
import vagueobjects.ir.lda.tokens.Vocabulary;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 12/6/13
 * Time: 8:06 PM
 * Project: IntelligentSearch
 */
public abstract class BaseExecutor implements InferenceTaskExecutor {
    protected final int topics;
    protected final int batchSize;
    protected BatchesReader batchesReader;
    protected final Stemmer stemmer;

    protected BaseExecutor(Stemmer stemmer, int topics, int batchSize) {
        this.stemmer = stemmer;
        this.topics = topics;
        this.batchSize = batchSize;
    }

    public final void start() throws Exception {
        preProcess();
        Vocabulary vocabulary = getCurrentVocabulary();
        TopicsModelAlgorithmFactory algorithmFactory = getTopicsModelAlgorithmFactory();
        batchesReader = getBatchesReader();
        processBatches(algorithmFactory, batchesReader, vocabulary);
        postProcess();
    }

    protected TopicsModelAlgorithmFactory getTopicsModelAlgorithmFactory() {
        return new DefaultTopicsModelAlgorithmFactory();
    }

    protected void processBatches(TopicsModelAlgorithmFactory algorithmFactory, BatchesReader batchesReader, Vocabulary vocabulary) throws Exception {
        List<DocumentData> docs;
        TopicModelAlgorithm lda = algorithmFactory.createTopicModel(vocabulary.size(), topics);
        int batch = 0;
        while(!(docs = batchesReader.getNextBatch()).isEmpty()) {
            batch ++;
            processSingleBatch(vocabulary, docs, lda, batch);
        }
    }

    protected void processSingleBatch(Vocabulary vocabulary, List<DocumentData> docs, TopicModelAlgorithm lda, int batch) throws IOException {
        OnlineLDASource documents = OnlineLDASource.createDocuments(docs, vocabulary, stemmer);
        OnlineLDAResult result = lda.workOn(documents);
        postProcessBatch(batch, result);
    }

    protected abstract BatchesReader getBatchesReader() throws Exception;
    protected abstract void preProcess();
    protected abstract Vocabulary getCurrentVocabulary() throws IOException;
    protected abstract void postProcessBatch(int batch, OnlineLDAResult result) throws IOException;
    protected abstract void postProcess() throws Exception;
}
