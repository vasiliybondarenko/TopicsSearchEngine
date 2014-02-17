package intelligence.core.engines.onlinelda;

import infrascructure.data.dao.ResourceMetaDataRepository;
import infrascructure.data.dao.ResultLinkDao;
import infrascructure.data.dom.Document;
import infrascructure.data.dom.ResourceMetaData;
import infrascructure.data.dom.Tags;
import intelligence.core.dao.DocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;

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
    @Qualifier(value = "plainDocMetaDataRepository")
    private ResourceMetaDataRepository resourceMetaDataRepository;

    @Autowired
    private ResultLinkDao resultLinkDao;

    @Autowired
    public OnlineLDAResultsWriter(DocumentsRepository documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    @Override
    public void saveDocumentsDistribution(Iterable<Document> documents) {
        ArrayList<Document> resultDocs = new ArrayList<>();
        for(Document doc: documents){
            ResourceMetaData resourceMetaData = resourceMetaDataRepository.findById(doc.getIdentifier());
            String url = resourceMetaData == null ? "UNDEFINED" : resourceMetaData.getTag(Tags.URL).getValue();

            doc.setUrl(url);
            resultDocs.add(doc);
        }

        documentsRepository.save(resultDocs);
    }
}
