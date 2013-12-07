package vagueobjects.ir.lda.online.execution;

import infrascructure.data.TaskExecutor;
import vagueobjects.ir.lda.online.TopicModelAlgorithm;
import vagueobjects.ir.lda.online.demo.*;
import vagueobjects.ir.lda.tokens.OnlineLDASource;
import vagueobjects.ir.lda.tokens.QuickVocabulary;
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
public abstract class BaseExecutor implements TaskExecutor{
    protected final int topics;
    protected final int batchSize;
    protected final BatchesReadersFactory batchesReaderFactory;

    protected BaseExecutor(int topics, int batchSize, BatchesReadersFactory batchesReadersFactory) {
        this.topics = topics;
        this.batchSize = batchSize;
        this.batchesReaderFactory = batchesReadersFactory;
    }

    public final void start() throws Exception {
        preProcess();
        try (BatchesReader batchesReader = batchesReaderFactory.createBatchesReader()){
            Vocabulary vocabulary = new QuickVocabulary(StandaloneOnlineLDAExecutor.getCurrentVocabulary());
            TopicsModelAlgorithmFactory algorithmFactory = new DefaultTopicsModelAlgorithmFactory();
            processBatches(algorithmFactory, batchesReader, vocabulary);
        }
    }

    protected void processBatches(TopicsModelAlgorithmFactory algorithmFactory, BatchesReader batchesReader, Vocabulary vocabulary) throws IOException {
        List<DocumentData> docs;
        TopicModelAlgorithm lda = algorithmFactory.createTopicModel(vocabulary.size(), topics);
        int batch = 0;
        while((docs = batchesReader.getNextBatch(batchSize)) != null) {
            batch ++;
            processSingleBatch(vocabulary, docs, lda, batch);
        }
        postProcess();
    }

    protected void processSingleBatch(Vocabulary vocabulary, List<DocumentData> docs, TopicModelAlgorithm lda, int batch) throws IOException {
        OnlineLDASource documents = OnlineLDASource.createDocuments(docs, vocabulary);
        OnlineLDAResult result = lda.workOn(documents);
        postProcessBatch(batch, result);
    }

    protected abstract void preProcess();
    protected abstract void postProcessBatch(int batch, OnlineLDAResult result) throws IOException;
    protected abstract void postProcess();
}
