package infrascructure.data.dao;

import infrascructure.data.dom.email.EmailMetaData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 5/18/14
 * Time: 3:29 PM
 * Project: NewTopicSearch
 */

@Repository
public interface EmailMetaDataRepository extends MongoRepository<EmailMetaData, Integer>, CustomEmailMetaDataRepository {
}
