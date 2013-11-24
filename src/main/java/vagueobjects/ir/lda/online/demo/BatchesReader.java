package vagueobjects.ir.lda.online.demo;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/21/13
 * Time: 12:58 AM
 * Project: IntelligentSearch
 */
public interface BatchesReader extends AutoCloseable{
    List<DocumentData> getNextBatch(int batchSize) throws IOException;
}
