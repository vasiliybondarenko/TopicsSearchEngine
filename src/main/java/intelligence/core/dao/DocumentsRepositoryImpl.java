package intelligence.core.dao;

import intelligence.core.util.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/17/13
 * Time: 11:16 PM
 * Project: IntelligentSearch
 */
@Repository
public class DocumentsRepositoryImpl implements CustomDocumentRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Document> getDocumentsByTopic(int topic, int limit) {
        return mongoTemplate.find(new Query().with(new Sort(Sort.Direction.DESC, "topicsDistribution." + topic)).limit(limit),
                Document.class);
    }

}
