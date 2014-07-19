package infrascructure.data.rss;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import infrascructure.data.dao.RSSFeedRepository;
import infrascructure.data.dom.rss.RssFeedItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 3/1/14
 * Time: 8:20 PM
 * Project: IntelligentSearch
 */
public class RssReader implements FeedReader{

    RSSFeedRepository repository;
    private final String location;
    private final String tag;

    protected RssReader(RSSFeedRepository repository, String location, String tag) {
        this.repository = repository;
        this.location = location;
        this.tag = tag;
    }

    @Override
    public List<RssFeedItem> read() throws Exception {
        URL url  = new URL(location);
        ArrayList<RssFeedItem> rssFeedItems = new ArrayList<>();

        try (XmlReader reader = new XmlReader(url)){
            SyndFeed feed = new SyndFeedInput().build(reader);
            for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
                SyndEntry entry = (SyndEntry) i.next();
                rssFeedItems.add(
                        new RssFeedItem(entry.getUri(), entry.getTitle(), entry.getDescription().getValue(), entry.getPublishedDate(), entry.getLink(), tag)
                );
            }
        }

        return repository.addNewFeeds(rssFeedItems);
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "RssReader{" +
                "location='" + location + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
