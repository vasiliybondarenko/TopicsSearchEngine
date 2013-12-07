package intelligence.core.dao;

import infrascructure.data.dom.Document;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/18/13
 * Time: 7:59 PM
 * Project: IntelligentSearch
 */
public interface CustomDocumentRepository {
    List<Document> getDocumentsByTopic(int topic, int limit);
}
