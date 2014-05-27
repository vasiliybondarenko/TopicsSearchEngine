package intelligence.core.engines;

import com.google.common.base.Preconditions;
import infrascructure.data.dao.RSSFeedRepository;
import infrascructure.data.dom.rss.RssFeedItem;
import infrascructure.data.util.Trace;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.Arrays;
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
        Preconditions.checkArgument(args.length > 0, "Number of arguments should be greater than 0. Each argument corresponds to rss feed title");

        String configPath = "rss/rssOnlineLDAContext.xml";
        String fullPath = new File(configPath).getAbsolutePath();

        Trace.trace("Configuration path: " + fullPath);

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("rss/rssOnlineLDAContext.xml");
        RSSFeedRepository repository = context.getBean(RSSFeedRepository.class);

        Arrays.stream(args).map(tag -> repository.getFeeds(2000, tag.trim())).forEach(feeds -> processRssFeeds(feeds));

    }

    private static void processRssFeeds(List<RssFeedItem> feeds){
        feeds.stream().limit(20).forEach((f) -> Trace.trace("%s - %s  %s", f.getPublishedDate(), f.getTitle(), f.getUrl()) );
        System.out.println(String.format("Total: %s", feeds.size()));
        System.out.println();
    }
}
