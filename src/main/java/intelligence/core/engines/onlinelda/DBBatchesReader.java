package intelligence.core.engines.onlinelda;

import infrascructure.data.dom.DocumentMetaData;
import infrascructure.data.util.IOHelper;
import intelligence.core.dao.DocumentMetaDataRepository;
import vagueobjects.ir.lda.online.demo.BatchesReader;
import vagueobjects.ir.lda.online.demo.DocumentData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 12/7/13
 * Time: 5:14 PM
 * Project: IntelligentSearch
 */
public class DBBatchesReader implements BatchesReader{
    private final int batchSize;
    private final DocumentMetaDataRepository repository;
    private List<DocumentMetaData> currentBatch;

    public DBBatchesReader(int batchSize, DocumentMetaDataRepository repository) {
        this.batchSize = batchSize;
        this.repository = repository;
        this.currentBatch = new ArrayList<>();
    }

    @Override
    public List<DocumentData> getNextBatch() throws IOException {
        markPreviousBatchAsProcessed();
        List<DocumentMetaData> unprocessedDocs = repository.getUnprocessedDocs(batchSize);
        currentBatch = unprocessedDocs;

        ArrayList<DocumentData> docs = new ArrayList<>();
        for (DocumentMetaData metaData: unprocessedDocs){
            String path = metaData.getFilePath();
            String title = metaData.getTitle();
            int id = metaData.getIdentifier();
            String text = IOHelper.readFromFile(path);
            docs.add(new DocumentData(id, title, text));
        }

        return docs;
    }

    @Override
    public boolean hasNextBatch() {
        return !repository.getUnprocessedDocs(batchSize).isEmpty();
    }

    private void markPreviousBatchAsProcessed() {
        repository.setAsProcessed(currentBatch);
    }
}
