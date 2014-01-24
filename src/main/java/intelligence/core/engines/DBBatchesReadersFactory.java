package intelligence.core.engines;

import intelligence.core.dao.DocumentMetaDataRepository;
import intelligence.core.engines.onlinelda.DBBatchesReader;
import org.springframework.beans.factory.annotation.Autowired;
import vagueobjects.ir.lda.online.demo.BatchesReader;
import vagueobjects.ir.lda.online.demo.BatchesReadersFactory;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/4/14
 * Time: 8:47 PM
 * Project: IntelligentSearch
 */
public class DBBatchesReadersFactory implements BatchesReadersFactory {
    private final int batchSize;

    @Autowired
    private DocumentMetaDataRepository repository;

    public DBBatchesReadersFactory(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public BatchesReader createBatchesReader() {
        return new DBBatchesReader(batchSize, repository);
    }
}
