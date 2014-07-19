package infrascructure.data.dao;

import com.google.common.base.Preconditions;
import infrascructure.data.dom.rss.RssFeedItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 3/1/14
 * Time: 6:38 PM
 * Project: IntelligentSearch
 */

public class RSSFeedRepositoryImpl implements RSSFeedRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    private final String collectionName;

    public RSSFeedRepositoryImpl(String collectionName) {
        Preconditions.checkArgument(collectionName != null, "collection name cannot be null");
        this.collectionName = collectionName;
    }


    @Override
    public List<RssFeedItem> getFeeds(int limit) {
        return getRssFeedItems(limit);
    }

    @Override
    public void addFeeds(List<RssFeedItem> feeds) {
        for (RssFeedItem feed : feeds) {
            saveItem(feed);
        }
    }

    @Override
    public void deleteAll() {
        mongoTemplate.remove(query(new Criteria()), collectionName);
    }

    @Override
    public List<RssFeedItem> addNewFeeds(List<RssFeedItem> feeds) {
        String tag = feeds.get(0).getTag();
        List<RssFeedItem> items = getFeeds(1, tag);
        List<RssFeedItem> newItems = new ArrayList<>();
        Date lastPublishedDate = new Date(0);
        if(!items.isEmpty()){
            RssFeedItem firstItem = items.get(0);
            lastPublishedDate = firstItem.getPublishedDate();
        }
        for (RssFeedItem feed : feeds) {
            if(feed.getPublishedDate().compareTo(lastPublishedDate) > 0){
                if(StringUtils.isNotEmpty(tag) && !tag.equals(feed.getTag())){
                    throw new RuntimeException("addNewFeeds method can't add feed items with different tags (expected tag " + tag + ") but actual: " + feed);
                }
                newItems.add(feed);
                saveItem(feed);
            }
        }
        return newItems;
    }

    @Override
    public List<RssFeedItem> getFeeds(int limit, String tag) {
        Query query = StringUtils.isNotEmpty(tag) ? query(where("tag").is(tag)) : query(new Criteria());
        return mongoTemplate.find(query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "publishedDate"))).limit(limit),
                RssFeedItem.class, collectionName);
    }

    @Override
    public List<RssFeedItem> getFeeds(int minId, int limit, String tag) {
        Preconditions.checkArgument(!StringUtils.isEmpty(tag));
        Query query = query(where("tag").is(tag)).addCriteria(where("_id").gte(String.valueOf(minId)));
        return mongoTemplate.find(query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "_id"))).limit(limit),
                RssFeedItem.class, collectionName);
    }

    protected List<RssFeedItem> getRssFeedItems(int limit) {
        return mongoTemplate.find(query(new Criteria()).with(new Sort(new Sort.Order(Sort.Direction.DESC, "publishedDate"))).limit(limit),
                RssFeedItem.class, collectionName);
    }

    protected void saveItem(RssFeedItem feed) {
        mongoTemplate.save(feed, collectionName);
    }
}
