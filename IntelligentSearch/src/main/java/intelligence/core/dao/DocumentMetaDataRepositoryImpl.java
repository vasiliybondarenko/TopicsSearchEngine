package intelligence.core.dao;

import infrascructure.data.dom.DocumentMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/11/14
 * Time: 5:42 PM
 * Project: IntelligentSearch
 */

@Repository
public class DocumentMetaDataRepositoryImpl implements CustomDocumentMetaDataRepository {


    private MongoTemplate mongoTemplate;

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<DocumentMetaData> getUnprocessedDocs(int batchSize) {
        return mongoTemplate.find(query(where("isProcessed").is(false)).with(new Sort(new Sort.Order(Sort.Direction.ASC, "_id"))).limit(batchSize),
                DocumentMetaData.class);
    }

    @Override
    public void setAsProcessed(List<DocumentMetaData> processedDocs) {
        for(DocumentMetaData doc: processedDocs){
            mongoTemplate.updateMulti(query(where("identifier").is(doc.getIdentifier())), Update.update("isProcessed", true),
                    DocumentMetaData.class);
        }
    }

    @Override
    public boolean hasUnprocessedDocs() {
        return mongoTemplate.findOne(query(where("isProcessed").is(false)).limit(1), DocumentMetaData.class) != null;
    }
}
