package intelligence.core.dao;

import infrascructure.data.dao.RssMetaDataRepository;
import infrascructure.data.dom.rss.RssMetaData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 7/19/14
 * Time: 3:25 PM
 * Project: NewTopicSearch
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:rss/test_mongodb.xml" })
public class RssMetaDataRepositoryTest {

    @Autowired
    private RssMetaDataRepository repository;

    @Test
    public void getLastFeedShouldReturnLastItemsSortedByIdASC() throws Exception {
        repository.deleteAll();
        ArrayList<RssMetaData> items = new ArrayList<RssMetaData>() {{
            add(new RssMetaData(1, "A"));
            add(new RssMetaData(2, "A"));
            add(new RssMetaData(3, "B"));
        }};
        repository.save(items);

        assertThat(repository.getLastItems(1, "A")).containsSequence(
            new RssMetaData(2, "A")
        );
    }

    @Test
    public void getLastFeedShouldReturnEmptyListIfNoItems() throws Exception {
        repository.deleteAll();

        assertThat(repository.getLastItems(1, "A")).isEmpty();
    }
}
