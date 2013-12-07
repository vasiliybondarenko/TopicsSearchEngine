package intelligence.core.util;

import infrascructure.data.dom.Document;
import infrascructure.data.dom.DocumentImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.CloseableStream;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/2/13
 * Time: 8:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentsParser {
    ArrayList<Document> getDocumentsFromFile(String path) throws IOException;

    CloseableStream<DocumentImpl> getDocumentsFromFileLazy(String path) throws IOException;
}
