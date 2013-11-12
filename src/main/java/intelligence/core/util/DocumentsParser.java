package intelligence.core.util;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/2/13
 * Time: 8:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentsParser {
    ArrayList<Document> readDocuments(String path) throws IOException;
}
