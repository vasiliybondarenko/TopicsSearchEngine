package intelligence.core.util;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/16/13
 * Time: 1:35 PM
 * Project: IntelligentSearch
 */
public class DocsStream implements AutoCloseable{
    private AutoCloseable autoCloseable;



    @Override
    public void close() throws Exception {
        autoCloseable.close();
    }
}
