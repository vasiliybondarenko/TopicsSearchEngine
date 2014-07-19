package infrastructure.data.crawl;

import infrascructure.data.crawl.RssURLIterator;
import infrascructure.data.dao.RSSFeedRepository;
import infrascructure.data.dao.RssMetaDataRepository;
import infrascructure.data.dom.rss.RssFeedItem;
import infrascructure.data.dom.rss.RssMetaData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 7/19/14
 * Time: 4:22 PM
 * Project: NewTopicSearch
 */

@RunWith(MockitoJUnitRunner.class)
public class RssURLIteratorTest {

    @Mock
    private RSSFeedRepository feedRepository;

    @Mock
    private RssMetaDataRepository logsRepository;

    @InjectMocks
    private RssURLIterator rssURLIterator;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void urlIteratorShouldReturnNullIfNoItems() throws Exception {
        assertThat(rssURLIterator.getNextURL()).isNull();
    }

    @Test
    public void urlIteratorShouldReturnNextItem() throws Exception {
        Date time = Calendar.getInstance().getTime();
        when(logsRepository.getLastItems(1, null)).thenReturn(new ArrayList<RssMetaData>(){{
            add(new RssMetaData(2, null));
        }});
        when(feedRepository.getFeeds(2, 10000, null)).thenReturn(new ArrayList<RssFeedItem>(){{
            add(new RssFeedItem("2", "", "", time, "url2", null));
            add(new RssFeedItem("3", "", "", time, "url3", null));
        }});

        assertThat(rssURLIterator.getNextURL()).isEqualTo("url2");

    }
}
