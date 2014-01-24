package infrascructure.data.integration;

import infrascructure.data.dom.DocumentMetaData;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/18/14
 * Time: 1:44 PM
 * Project: IntelligentSearch
 */
public interface DocumentMetaDataReader {
    Iterator<DocumentMetaData> readDocumentMetaData() throws IOException;
}
