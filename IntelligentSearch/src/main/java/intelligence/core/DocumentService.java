package intelligence.core;

import infrascructure.data.dom.Document;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/18/13
 * Time: 12:29 AM
 * Project: IntelligentSearch
 */
public interface DocumentService {
    List<Document> getTopDocumentsForTopic(int topic);
}
