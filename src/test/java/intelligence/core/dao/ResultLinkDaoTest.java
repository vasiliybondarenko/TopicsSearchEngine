package intelligence.core.dao;


import infrascructure.data.dao.ResultLinkDao;
import infrascructure.data.email.html.entity.ResultLink;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/9/14
 * Time: 10:10 AM
 * Project: IntelligentSearch
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:localSample/test_mongodb.xml" })
public class ResultLinkDaoTest {

    @Autowired
    private ResultLinkDao resultLinkDao;

    @Test
    public void resultLinkShouldSaved() throws Exception {
        ResultLink link = new ResultLink("url1", "title1");

        resultLinkDao.save(link);

        assertThat(resultLinkDao.findAll()).isNotEmpty();
    }

    @Test
    public void getUrlByDocIdTest() throws Exception {
        ResultLink link = new ResultLink("url1", "title1");
        link.setRawDocId("1");

        resultLinkDao.save(link);

        assertThat(resultLinkDao.findByrawDocId("1"));
    }

    @Test
    public void findByUrlTest() throws Exception {
        ResultLink link = new ResultLink("url1", "title1");
        link.setRawDocId("1");

        resultLinkDao.save(link);

        assertThat(resultLinkDao.findByUrl("url1").getRawDocId()).isEqualToIgnoringCase("1");
    }

    @Before
    public void setUp() throws Exception {
        resultLinkDao.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        resultLinkDao.deleteAll();
    }
}
