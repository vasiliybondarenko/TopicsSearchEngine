package intelligence.core.engines;

import infrascructure.data.dom.DocumentMetaData;
import infrascructure.data.integration.DirectoryDocumentMetaDataReader;
import intelligence.core.dao.DocumentMetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/20/14
 * Time: 5:22 PM
 * Project: IntelligentSearch
 */
public class FileToDBInferenceContextSynchronizer implements InferenceContextSynchronizer {

    private final DocumentMetaDataRepository repository;
    private final DirectoryDocumentMetaDataReader documentMetaDataReader;

    @Autowired
    public FileToDBInferenceContextSynchronizer(DocumentMetaDataRepository repository, DirectoryDocumentMetaDataReader documentMetaDataReader) {
        this.repository = repository;
        this.documentMetaDataReader = documentMetaDataReader;
    }

    @Override
    public void synchronize() throws Exception {
        try{
            Iterator<DocumentMetaData> documentMetaDataIterator = documentMetaDataReader.readDocumentMetaData();
            while (documentMetaDataIterator.hasNext()){
                repository.save(documentMetaDataIterator.next());
            }
        }finally {
             documentMetaDataReader.close();
        }

    }
}
