package intelligence.core.dao;

import infrascructure.data.dom.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/13/13
 * Time: 9:06 PM
 * Project: IntelligentSearch
 */
public interface DocumentsRepository extends MongoRepository<Document, Integer>, CustomDocumentRepository {
}
