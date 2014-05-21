package infrascructure.data.rss;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 5/21/14
 * Time: 11:21 PM
 * Project: NewTopicSearch
 */
public interface RssReaderFactory {
    FeedReader createReader(String location, String tag);
}
