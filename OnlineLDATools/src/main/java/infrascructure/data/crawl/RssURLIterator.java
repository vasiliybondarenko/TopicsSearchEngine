package infrascructure.data.crawl;

import infrascructure.data.dao.RSSFeedRepository;
import infrascructure.data.dao.RssMetaDataRepository;
import infrascructure.data.dom.rss.RssFeedItem;
import infrascructure.data.dom.rss.RssMetaData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 3/1/14
 * Time: 9:15 PM
 * Project: IntelligentSearch
 */
public class RssURLIterator implements URLIterator {

    private final String rssTag;

    @Autowired
    private RSSFeedRepository feedRepository;

    @Autowired
    private RssMetaDataRepository logsRepository;

    private Queue<RssFeedItem> feedsQueue;

    public RssURLIterator(String rssTag) {
        this.rssTag = rssTag;
    }

    private void init(){
        List<RssMetaData> lastLoggedItems = logsRepository.getLastItems(1, rssTag);
        int lastId = lastLoggedItems.size() > 0 ? lastLoggedItems.get(0).getId() : 0;
        List<RssFeedItem> feeds = feedRepository.getFeeds(lastId, 10000, rssTag);
        feedsQueue = new LinkedList<>(feeds);
    }

    @Override
    public String getNextURL() {
        if(feedsQueue == null){
            init();
        }
        return feedsQueue.isEmpty() ? null : feedsQueue.poll().getUrl();
    }
}
