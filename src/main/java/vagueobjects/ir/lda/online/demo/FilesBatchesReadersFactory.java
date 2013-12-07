package vagueobjects.ir.lda.online.demo;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 12/7/13
 * Time: 4:17 PM
 * Project: IntelligentSearch
 */
public class FilesBatchesReadersFactory implements BatchesReadersFactory {
    @Override
    public BatchesReader createBatchesReader() {
        return new FileSystemBatchesReader();
    }
}
