package infrascructure.data.rss;

import infrascructure.data.dom.rss.RssFeedItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 5/21/14
 * Time: 11:34 PM
 * Project: NewTopicSearch
 */
public interface FeedReader {
    List<RssFeedItem> read() throws Exception;

    String getTag();
}
