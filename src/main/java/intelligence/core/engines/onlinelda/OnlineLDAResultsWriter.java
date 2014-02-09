package intelligence.core.engines.onlinelda;

import infrascructure.data.dao.ResultLinkDao;
import infrascructure.data.dom.Document;
import infrascructure.data.email.html.entity.ResultLink;
import infrascructure.data.util.Trace;
import intelligence.core.dao.DocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;

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
    private ResultLinkDao resultLinkDao;

    @Autowired
    public OnlineLDAResultsWriter(DocumentsRepository documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    @Override
    public void saveDocumentsDistribution(Iterable<Document> documents) {
        ArrayList<Document> resultDocs = new ArrayList<>();
        for(Document doc: documents){
            ResultLink link = resultLinkDao.findByrawDocId(String.valueOf(doc.getIdentifier()));
            if(link == null){
                Trace.trace("URL was not found for "  + doc.getIdentifier());
                continue;
            }
            doc.setUrl(link.getUrl());
            resultDocs.add(doc);
        }

        documentsRepository.save(resultDocs);
    }
}
