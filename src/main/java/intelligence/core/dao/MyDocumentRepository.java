package intelligence.core.dao;

import intelligence.core.util.Document;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/18/13
 * Time: 7:59 PM
 * Project: IntelligentSearch
 */
public interface MyDocumentRepository {
    List<Document> getDocumentsByTopic(int topic);
}
