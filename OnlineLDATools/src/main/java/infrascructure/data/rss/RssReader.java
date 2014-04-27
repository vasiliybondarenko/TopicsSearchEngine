package infrascructure.data.rss;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import infrascructure.data.dao.RSSFeedRepository;
import infrascructure.data.dom.rss.RssFeedItem;
import org.springframework.beans.factory.annotation.Autowired;

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
public class RssReader {

    @Autowired
    private RSSFeedRepository repository;

    private final String location;
    private final String tag;

    public RssReader(String location, String tag) {
        this.location = location;
        this.tag = tag;
    }

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

    public String getTag() {
        return tag;
    }
}
