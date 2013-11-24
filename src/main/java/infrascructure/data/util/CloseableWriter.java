package infrascructure.data.util;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/23/13
 * Time: 10:26 PM
 * Project: IntelligentSearch
 */
public interface CloseableWriter extends AutoCloseable{
    void appendLine(String line);
    void close() throws IOException;
}
