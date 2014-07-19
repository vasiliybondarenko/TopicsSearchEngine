package infrascructure.data.dao;

import infrascructure.data.dom.rss.RssMetaData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 7/18/14
 * Time: 8:59 PM
 * Project: NewTopicSearch
 */

@Repository
public interface RssMetaDataRepository extends MongoRepository<RssMetaData, Integer>, CustomRssMetaDataRepository{
}
