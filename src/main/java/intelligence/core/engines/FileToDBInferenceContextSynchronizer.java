package intelligence.core.engines;

import infrascructure.data.dom.DocumentMetaData;
import infrascructure.data.integration.DirectoryDocumentMetaDataReader;
import infrascructure.data.util.IOHelper;
import intelligence.core.dao.DocumentMetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
        ArrayList<String> titles = new ArrayList<>();
        try{
            Iterator<DocumentMetaData> documentMetaDataIterator = documentMetaDataReader.readDocumentMetaData();
            while (documentMetaDataIterator.hasNext()){
                DocumentMetaData next = documentMetaDataIterator.next();
                repository.save(next);
                titles.add(next.getTitle());
            }
            String path = "/Users/shredinger/Documents/DEVELOPMENT/Projects/SHARED/OnlineLDA-Launch/data/OnlineLDA_Test_SampleData/Results/processed_docs.txt";
            IOHelper.writeLinesToFile(path, titles);
        }finally {
             documentMetaDataReader.close();
        }

    }
}
