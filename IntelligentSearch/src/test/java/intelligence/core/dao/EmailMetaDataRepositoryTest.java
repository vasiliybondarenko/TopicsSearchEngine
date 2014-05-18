package intelligence.core.dao;

import infrascructure.data.dao.EmailMetaDataRepository;
import infrascructure.data.dom.email.EmailMetaData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 5/18/14
 * Time: 3:35 PM
 * Project: NewTopicSearch
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:localSample/test_mongodb.xml" })
public class EmailMetaDataRepositoryTest {

    @Autowired
    private EmailMetaDataRepository repository;

    @Before
    public void setUp() throws Exception {
        repository.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void testSave() throws Exception {
        Date date = Calendar.getInstance().getTime();
        EmailMetaData emailMetaData = new EmailMetaData(1, "", date, "");

        repository.save(emailMetaData);

        assertThat(repository.findOne(1)).isEqualTo(emailMetaData);
    }

    @Test
    public void testGetLatestItems() throws Exception {
        Date date = Calendar.getInstance().getTime();
        EmailMetaData emailMetaData1 = new EmailMetaData(1, "", date, "");
        EmailMetaData emailMetaData2 = new EmailMetaData(2, "", date, "");
        ArrayList<EmailMetaData> items = new ArrayList<EmailMetaData>() {{
            add(emailMetaData2);
            add(emailMetaData1);
        }};

        repository.save(items);

        assertThat(repository.getLastItems(1)).containsSequence(emailMetaData2);
    }
}
