package intelligence.core.dao;

import infrascructure.data.dao.RSSFeedRepository;
import infrascructure.data.dom.rss.RssFeedItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 3/1/14
 * Time: 6:52 PM
 * Project: IntelligentSearch
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:rss/test_mongodb.xml" })
public class RSSFeedRepositoryTest {

    @Autowired
    private RSSFeedRepository repository;

    @Test
    public void testGetFeeds() throws Exception {
        repository.deleteAll();
        List<RssFeedItem> feedsToAdd = new ArrayList<>();
        feedsToAdd.add(new RssFeedItem("1", "", "", new Date(), "", ""));
        repository.addFeeds(feedsToAdd);

        List<RssFeedItem> feeds = repository.getFeeds(1);

        assertThat(feeds).isNotEmpty();
    }

    @Test
    public void getFeedsShouldReturnOrderedByDateFeeds() throws Exception {
        repository.deleteAll();
        List<RssFeedItem> feedsToAdd = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.HOUR_OF_DAY, 1);
        Calendar calendar2 = Calendar.getInstance();
        calendar1.add(Calendar.HOUR_OF_DAY, 2);
        feedsToAdd.add(new RssFeedItem("1", "", "", calendar1.getTime(), "", ""));
        feedsToAdd.add(new RssFeedItem("2", "", "", calendar2.getTime(), "", ""));

        repository.addFeeds(feedsToAdd);
        List<RssFeedItem> feeds = repository.getFeeds(2);

        assertThat(feeds).containsSequence(
                new RssFeedItem("1", "", "", calendar1.getTime(), "", ""),
                new RssFeedItem("2", "", "", calendar2.getTime(), "", "")
        );
    }

    @Test
    public void addNewFeedsShouldAddOnlyNewFeeds() throws Exception {
        repository.deleteAll();
        List<RssFeedItem> feedsToAdd = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.HOUR_OF_DAY, 1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.HOUR_OF_DAY, 2);
        feedsToAdd.add(new RssFeedItem("1", "", "", calendar1.getTime(), "", ""));
        repository.addFeeds(feedsToAdd);

        repository.addNewFeeds(new ArrayList<RssFeedItem>(){{
            add(new RssFeedItem("2", "", "", calendar1.getTime(), "", ""));
            add(new RssFeedItem("3", "", "", calendar2.getTime(), "", ""));
        }});


        assertThat(repository.getFeeds(3)).containsSequence(
                new RssFeedItem("3", "", "", calendar2.getTime(), "", ""),
                new RssFeedItem("1", "", "", calendar1.getTime(), "", "")
        );
    }

    @Test
    public void getFeedsByTagShouldReturnFeedsByTag() throws Exception {
        repository.deleteAll();
        List<RssFeedItem> feedsToAdd = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.HOUR_OF_DAY, 1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.HOUR_OF_DAY, 2);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.HOUR_OF_DAY, 3);
        feedsToAdd.add(new RssFeedItem("1", "", "", calendar1.getTime(), "", "tag1"));
        feedsToAdd.add(new RssFeedItem("2", "", "", calendar2.getTime(), "", "tag1"));
        feedsToAdd.add(new RssFeedItem("3", "", "", calendar3.getTime(), "", "tag2"));

        repository.addFeeds(feedsToAdd);
        List<RssFeedItem> feeds = repository.getFeeds(2, "tag1");

        assertThat(feeds).containsSequence(
                new RssFeedItem("2", "", "", calendar2.getTime(), "", "tag1"),
                new RssFeedItem("1", "", "", calendar1.getTime(), "", "tag1")
        );

    }

    @Test
    public void getFeedsByTagShouldReturnFeedsSortedById() throws Exception {
        repository.deleteAll();
        List<RssFeedItem> feedsToAdd = new ArrayList<>();
        feedsToAdd.add(new RssFeedItem("1", "", "", Calendar.getInstance().getTime(), "", "A"));
        feedsToAdd.add(new RssFeedItem("2", "", "", Calendar.getInstance().getTime(), "", "A"));
        feedsToAdd.add(new RssFeedItem("3", "", "", Calendar.getInstance().getTime(), "", "A"));
        repository.addFeeds(feedsToAdd);

        List<RssFeedItem> actualItems = repository.getFeeds(2, 2, "A");

        assertThat(actualItems).containsSequence(
          feedsToAdd.get(1),
          feedsToAdd.get(2)
        );
    }
}
