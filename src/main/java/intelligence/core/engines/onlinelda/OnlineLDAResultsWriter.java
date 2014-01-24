package intelligence.core.engines.onlinelda;

import infrascructure.data.dom.Document;
import intelligence.core.dao.DocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/24/14
 * Time: 8:03 PM
 * Project: IntelligentSearch
 */
public class OnlineLDAResultsWriter implements InferenceResultWriter {
    private final DocumentsRepository documentsRepository;

    @Autowired
    public OnlineLDAResultsWriter(DocumentsRepository documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    @Override
    public void saveDocumentsDistribution(Iterable<Document> documents) {
        documentsRepository.save(documents);
    }
}
