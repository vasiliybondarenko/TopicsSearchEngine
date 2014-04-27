package intelligence.core.engines;

import infrascructure.data.dao.RSSFeedRepository;
import infrascructure.data.dom.rss.RssFeedItem;
import infrascructure.data.util.Trace;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 3/2/14
 * Time: 6:29 PM
 * Project: IntelligentSearch
 */
public class RSSReaderShowAll {
    public static void main(String[] args) {
        String configPath = "IntelligentSearch/src/main/resources/rss/rssOnlineLDAContext.xml";
        String fullPath = new File(configPath).getAbsolutePath();

        Trace.trace("Configuration path: " + fullPath);

        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(configPath);
        RSSFeedRepository repository = context.getBean(RSSFeedRepository.class);
        List<RssFeedItem> spaceFeeds = repository.getFeeds(1000, "space.com");
        int spaceTotal = spaceFeeds.size();

        List<RssFeedItem> dzoneFeeds = repository.getFeeds(1000, "dzone");
        int dzoneTotal = dzoneFeeds.size();

        spaceFeeds.stream().limit(20).forEach((f) -> System.out.println(f.getPublishedDate() + " - " + f.getTitle() + "   " + f.getUrl()));
        System.out.println("Space.com total: " + spaceTotal);

        dzoneFeeds.stream().limit(20).forEach((f) -> System.out.println(f.getPublishedDate() + " - " + f.getTitle() + "   " + f.getUrl()));
        System.out.println("DZone total: " + dzoneTotal);
    }
}
