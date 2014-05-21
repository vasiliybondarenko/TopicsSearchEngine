package infrascructure.data.rss;

import infrascructure.data.dao.RSSFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 5/21/14
 * Time: 11:23 PM
 * Project: NewTopicSearch
 */
public class DefaultRssReaderFactory implements RssReaderFactory {

    @Autowired
    private RSSFeedRepository repository;

    @Override
    public FeedReader createReader(String location, String tag) {
        return new RssReader(repository, location, tag);
    }
}
