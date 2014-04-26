package intelligence.core.engines.onlinelda;

import infrascructure.data.Config;
import infrascructure.data.dom.Document;
import infrascructure.data.stripping.Stemmer;
import infrascructure.data.util.CloseableWriter;
import infrascructure.data.util.DefaultFileWriter;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;
import intelligence.core.dao.DocumentsRepository;
import intelligence.core.engines.InferenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import vagueobjects.ir.lda.online.TopicModelAlgorithm;
import vagueobjects.ir.lda.online.demo.*;
import vagueobjects.ir.lda.online.execution.BaseExecutor;
import vagueobjects.ir.lda.tokens.QuickVocabulary;
import vagueobjects.ir.lda.tokens.Vocabulary;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/20/13
 * Time: 9:56 PM
 * Project: IntelligentSearch
 */
public class OnlineLDAExecutor extends BaseExecutor {

    @Autowired
    private Config config;

    private final InferenceContext context;
    private final InferenceResultWriter resultWriter;

    @Autowired
    private DocumentsRepository documentsRepository;

    public OnlineLDAExecutor(Stemmer stemmer, int topics, int batchSize, InferenceContext context, InferenceResultWriter resultWriter) {
        super(stemmer, topics, batchSize);
        this.context = context;
        this.resultWriter = resultWriter;
    }

    @Override
    protected void processBatches(TopicsModelAlgorithmFactory algorithmFactory, BatchesReader batchesReader, Vocabulary vocabulary) throws Exception {
        super.processBatches(algorithmFactory, batchesReader, vocabulary);
    }

    @Override
    protected void processSingleBatch(Vocabulary vocabulary, List<DocumentData> docs, TopicModelAlgorithm lda, int batch) throws IOException {
        super.processSingleBatch(vocabulary, docs, lda, batch);
    }

    @Override
    protected BatchesReader getBatchesReader() throws Exception{
        return context.getBatchesReader();
    }

    @Override
    protected void preProcess() {
    }

    @Override
    protected void postProcessBatch(int batch, OnlineLDAResult result) throws IOException {
        Trace.trace("Batch " + batch + " was processed");

        String topWordsPath = config.getProperty(Config.ONLINELDA_RESULTS_PATH);
        saveTopWords(result, topWordsPath, new DefaultOnlineLDAResultWriter());

        resultWriter.saveDocumentsDistribution(result.getDocuments());

        String docsPath = config.getProperty(Config.ONLINELDA_RESULTS_DOCS_PATH);
        int topics = config.getPropertyInt(Config.TOPICS);

        try(PrintWriter writer = new PrintWriter(docsPath)){
            for (int i = 0; i < topics; i++) {
                List<Document> documentsByTopic = documentsRepository.getDocumentsByTopic(i, 100);
                writer.println("\n\nTOPIC " + i + ":\n");
                documentsByTopic.forEach(writer::println);
            }
        }

    }

    @Override
    protected QuickVocabulary getCurrentVocabulary() throws IOException {
        String path = config.getProperty("vocabulary_path");
        List<String> words = IOHelper.readLinesFromFile(path);
        return new QuickVocabulary(words);
    }

    @Override
    protected void postProcess() {
        Trace.trace("Inference completed successfully");
    }

    private void saveTopWords(OnlineLDAResult result, String topWordsPath, OnlineLDAResultWriter ldaResultWriter) throws IOException {
        try(CloseableWriter topWordsWriter = new DefaultFileWriter(topWordsPath)){
            ldaResultWriter.writeTopWords(result, topWordsWriter, OnlineLDAResult.NUMBER_OF_TOKENS);
        }
    }
}
