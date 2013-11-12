package intelligence.core.dao;

import intelligence.core.util.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/13/13
 * Time: 9:06 PM
 * Project: IntelligentSearch
 */
public interface DocumentsRepository extends MongoRepository<Document, Integer> {
    @Query(value = "{}, {}, {'topicsDistribution.0':1}")
    List<Document> getDocumentsByTopic(int topic);
}
