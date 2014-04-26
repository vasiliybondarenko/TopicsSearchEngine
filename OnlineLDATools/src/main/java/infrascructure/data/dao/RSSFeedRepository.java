package infrascructure.data.dao;

import infrascructure.data.dom.rss.RssFeedItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 3/1/14
 * Time: 6:38 PM
 * Project: IntelligentSearch
 */
public interface RSSFeedRepository {
    List<RssFeedItem> getFeeds(int limit);

    void addFeeds(List<RssFeedItem> feeds);

    void deleteAll();

    List<RssFeedItem> addNewFeeds(List<RssFeedItem> feeds);

    List<RssFeedItem> getFeeds(int limit, String tag);
}
