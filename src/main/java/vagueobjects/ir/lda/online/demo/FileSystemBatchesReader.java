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

    public FileSystemBatchesReader() {
        this.docsRepository = new SimpleDocsRepository();
    }

    @Override
    public List<DocumentData> getNextBatch(int batchSize) throws IOException {
        return docsRepository.getNextBatch(batchSize);
    }

    @Override
    public void close() throws Exception {
        docsRepository.close();
    }
}
