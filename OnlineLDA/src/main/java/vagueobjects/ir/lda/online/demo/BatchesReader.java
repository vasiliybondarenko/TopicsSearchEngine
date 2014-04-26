package vagueobjects.ir.lda.online.demo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/21/13
 * Time: 12:58 AM
 * Project: IntelligentSearch
 */
public interface BatchesReader{
    List<DocumentData> getNextBatch() throws Exception;
    boolean hasNextBatch();
}
