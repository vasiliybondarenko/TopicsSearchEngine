package vagueobjects.ir.lda.online.execution;

import infrascructure.data.util.CloseableWriter;
import infrascructure.data.util.DefaultFileWriter;
import infrascructure.data.util.Trace;
import vagueobjects.ir.lda.online.Config;
import vagueobjects.ir.lda.online.Result;
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
* Date: 12/11/13
* Time: 9:53 PM
* Project: IntelligentSearch
*/
public class LocalOnlineLDAExecutor extends BaseExecutor{

    protected final BatchesReadersFactory batchesReaderFactory;

    public LocalOnlineLDAExecutor(int topics, int batchSize, BatchesReadersFactory batchesReadersFactory) {
        super(topics, batchSize);
        this.batchesReaderFactory = batchesReadersFactory;
    }

    @Override
    protected void processBatches(TopicsModelAlgorithmFactory algorithmFactory, BatchesReader batchesReader, Vocabulary vocabulary) throws Exception {
        Trace.trace("Online LDA initializing ...");
        Trace.trace("Topics: " + topics);
        Trace.trace("Vocabulary size: " + vocabulary.size());
        Trace.trace("Batch size: " + batchSize);
        List<DocumentData> docs;
        TopicModelAlgorithm lda = algorithmFactory.createTopicModel(vocabulary.size(), topics);
        int batch = 0;
        while(batchesReader.hasNextBatch()) {
            batch ++;
            docs = batchesReader.getNextBatch();
            processSingleBatch(vocabulary, docs, lda, batch);
        }
        postProcess();
    }

    @Override
    protected void processSingleBatch(Vocabulary vocabulary, List<DocumentData> docs, TopicModelAlgorithm lda, int batch) throws IOException {
        Trace.trace("==================== Batch " + batch++ + " ========================");
        Trace.trace("Read " + docs.size() + " docs");
        OnlineLDASource documents = OnlineLDASource.createDocuments(docs, vocabulary);
        Trace.trace("OnlineLDA is starting ...");

        StandaloneOnlineLDAExecutor.ExecutionResult<Result> executionResult = StandaloneOnlineLDAExecutor.Exec.execute(lda::workOn, documents);
        OnlineLDAResult result = executionResult.getResult();
        long executionTime = executionResult.getExecutionTime();

        Trace.trace("Writing results... Batch execution time: " + (executionTime / 1000000000) + "sec");
        postProcessBatch(batch, result);
    }

    @Override
    protected void preProcess() {
        batchesReader = getBatchesReader();
        Trace.trace("Loading ...");
    }

    @Override
    protected BatchesReader getBatchesReader() {
        return batchesReaderFactory.createBatchesReader();
    }

    @Override
    protected void postProcess() throws IOException {
        Trace.trace("Stopped. No docs available");
    }

    protected QuickVocabulary getCurrentVocabulary() throws IOException {
        return new QuickVocabulary(StandaloneOnlineLDAExecutor.getCurrentVocabulary());
    }

    @Override
    protected void postProcessBatch(int batch, OnlineLDAResult result) throws IOException {
        String docsDistributionPath = String.format("%s_batch=%d.txt", Config.getProperty("onlinelds.results.docs"), batch);
        String topWordsPath = Config.getProperty("onlinelds.results");

        OnlineLDAResultWriter ldaResultWriter = new DefaultOnlineLDAResultWriter();

        saveTopWords(result, topWordsPath, ldaResultWriter);
        saveDocsDistribution(result, docsDistributionPath, ldaResultWriter);
    }

    private void saveDocsDistribution(OnlineLDAResult result, String docsDistributionPath, OnlineLDAResultWriter ldaResultWriter) throws IOException{
        try (DefaultFileWriter docWriter = new DefaultFileWriter(docsDistributionPath)){
            ldaResultWriter.writeDocumentTopicsDistribution(result, docWriter);
        }
    }

    private void saveTopWords(OnlineLDAResult result, String topWordsPath, OnlineLDAResultWriter ldaResultWriter) throws IOException {
        try(CloseableWriter topWordsWriter = new DefaultFileWriter(topWordsPath)){
            ldaResultWriter.writeTopWords(result, topWordsWriter, OnlineLDAResult.NUMBER_OF_TOKENS);
        }
    }
}
