package infrascructure.data.dao;

import infrascructure.data.dom.email.EmailMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 5/18/14
 * Time: 3:51 PM
 * Project: NewTopicSearch
 */


public class EmailMetaDataRepositoryImpl implements CustomEmailMetaDataRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<EmailMetaData> getLastItems(int limit) {
        return mongoTemplate.find(new Query().with(new Sort(Sort.Direction.DESC, "_id")).limit(limit), EmailMetaData.class);
    }
}
