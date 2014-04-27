package intelligence.core.engines;

import infrascructure.data.dom.rss.RssFeedItem;
import infrascructure.data.rss.RssReader;
import infrascructure.data.util.Trace;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 3/1/14
 * Time: 9:06 PM
 * Project: IntelligentSearch
 */
public class RSSReader {
    public static void main(String[] args) throws Exception {

        String configPath = "IntelligentSearch/src/main/resources/rss/rssOnlineLDAContext.xml";
        String fullPath = new File(configPath).getAbsolutePath();

        Trace.trace("Configuration path: " + fullPath);

        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(configPath);
        List<RssReader> rssReaders = (List<RssReader>)context.getBean("rssReaders");

        for (RssReader reader : rssReaders) {
            List<RssFeedItem> newItems = reader.read();
            System.out.println(String.format("[%s] NEW FEEDS:", reader.getTag()));
            newItems.forEach((f) -> System.out.println(f.getPublishedDate() + " - " + f.getTitle()));
            System.out.println("------------------------------------------\n\n");
        }

    }
}
