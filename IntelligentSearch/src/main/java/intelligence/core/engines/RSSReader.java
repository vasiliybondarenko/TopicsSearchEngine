package intelligence.core.engines;

import com.google.common.base.Preconditions;
import infrascructure.data.dom.rss.RssFeedItem;
import infrascructure.data.rss.FeedReader;
import infrascructure.data.rss.RssReaderFactory;
import infrascructure.data.util.Trace;
import intelligence.core.util.GeneralConsts;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.Arrays;
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

        Preconditions.checkArgument(args.length > 0, "RSSReader expects non empty arguments list");

        String configPath = "rss/rssOnlineLDAContext.xml";
        String fullPath = new File(configPath).getAbsolutePath();

        Trace.trace("Configuration path: " + fullPath);

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("rss/rssOnlineLDAContext.xml");
        RssReaderFactory rssReaderFactory = context.getBean(RssReaderFactory.class);
        Arrays.asList(args).stream().map(s -> parseArgument(rssReaderFactory, s)).forEach(reader -> processReader(reader));


    }

    private static void processReader(FeedReader reader){
        try {
            List<RssFeedItem> newItems = reader.read();
            Trace.trace(String.format("[%s] NEW FEEDS:", reader.getTag()));
            newItems.forEach((f) -> Trace.trace("%s - %s", f.getPublishedDate(), f.getTitle()));
            System.out.println("------------------------------------------\n\n");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static FeedReader parseArgument(RssReaderFactory factory, String arg){
        String[] parts = arg.split(GeneralConsts.RSS_READER_ARGUMENTS_SEPARATOR_REGEX);
        Preconditions.checkArgument(parts.length == 2, "Incorrect argument format: " + arg + ". Expected {rss location}" + GeneralConsts.RSS_READER_ARGUMENTS_SEPARATOR_REGEX + "rss title");
        return factory.createReader(parts[0].trim(), parts[1].trim());
    }
}
