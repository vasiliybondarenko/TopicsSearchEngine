package infrascructure.data.dao;

import infrascructure.data.dom.rss.RssMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 7/18/14
 * Time: 11:54 PM
 * Project: NewTopicSearch
 */
public class RssMetaDataRepositoryImpl implements CustomRssMetaDataRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<RssMetaData> getLastItems(int limit, String tag) {
        return mongoTemplate.find(new Query(where("tag").is(tag)).with(new Sort(Sort.Direction.DESC, "_id")).limit(limit), RssMetaData.class);
    }
}
