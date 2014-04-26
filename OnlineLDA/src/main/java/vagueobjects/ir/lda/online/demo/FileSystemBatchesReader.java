package vagueobjects.ir.lda.online.demo;

import vagueobjects.ir.lda.online.execution.SimpleDocsRepository;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/21/13
 * Time: 1:00 AM
 * Project: IntelligentSearch
 */
public class FileSystemBatchesReader implements BatchesReader{

    private SimpleDocsRepository docsRepository;
    private final int batchSize;

    public FileSystemBatchesReader(int batchSize) {
        this.batchSize = batchSize;
        this.docsRepository = new SimpleDocsRepository(batchSize);
    }

    @Override
    public List<DocumentData> getNextBatch() throws IOException {
        return docsRepository.getNextBatch();
    }

    @Override
    public boolean hasNextBatch(){
        return docsRepository.hasNextBatch();
    }
}
