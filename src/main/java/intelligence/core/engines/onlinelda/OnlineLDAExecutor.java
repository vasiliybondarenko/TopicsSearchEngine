package intelligence.core.engines.onlinelda;

import vagueobjects.ir.lda.online.TopicModelAlgorithm;
import vagueobjects.ir.lda.online.demo.*;
import vagueobjects.ir.lda.online.execution.BaseExecutor;
import vagueobjects.ir.lda.tokens.Vocabulary;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/20/13
 * Time: 9:56 PM
 * Project: IntelligentSearch
 */
public class OnlineLDAExecutor extends BaseExecutor {

    public OnlineLDAExecutor(int topics, int batchSize, BatchesReadersFactory batchesReaderFactory) {
        super(topics, batchSize, batchesReaderFactory);
    }

    @Override
    protected void processBatches(TopicsModelAlgorithmFactory algorithmFactory, BatchesReader batchesReader, Vocabulary vocabulary) throws IOException {
        super.processBatches(algorithmFactory, batchesReader, vocabulary);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void processSingleBatch(Vocabulary vocabulary, List<DocumentData> docs, TopicModelAlgorithm lda, int batch) throws IOException {
        super.processSingleBatch(vocabulary, docs, lda, batch);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void preProcess() {
        throw new RuntimeException("Implement me!");
    }

    @Override
    protected void postProcessBatch(int batch, OnlineLDAResult result) throws IOException {
        throw new RuntimeException("Implement me!");
    }

    @Override
    protected void postProcess() {
        throw new RuntimeException("Implement me!");
    }
}
