package intelligence.core.dao;

import infrascructure.data.dom.DocumentMetaData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/11/14
 * Time: 4:24 PM
 * Project: IntelligentSearch
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test_mongodb.xml" })
public class DocumentMetaDataRepositoryTest {

    @Autowired
    private DocumentMetaDataRepository repository;

    @Before
    public void setUp() throws Exception {
        repository.deleteAll();
    }

    @After
    public void cleanDB() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void testSave() throws Exception {
        DocumentMetaData documentMetaData = new DocumentMetaData(1, "test", "path", false);

        repository.save(documentMetaData);

        assertThat(repository.findOne(1)).isEqualTo(documentMetaData);
    }

    @Test
    public void testGetBatch() throws Exception {
        repository.save(new ArrayList<DocumentMetaData>(){{
            add(new DocumentMetaData(1, "1", "1", false));
            add(new DocumentMetaData(2, "2", "2", false));
            add(new DocumentMetaData(3, "3", "3", false));
        }});

        List<DocumentMetaData> docs = repository.getUnprocessedDocs(2);

        ArrayList<DocumentMetaData> expected = new ArrayList<DocumentMetaData>() {{
                add(new DocumentMetaData(1, "1", "1", false));
                add(new DocumentMetaData(2, "2", "2", false));
            }
        };
        assertThat(docs).isEqualTo(expected);
    }

    @Test
    public void testSetAsProcessed() throws Exception {
        ArrayList<DocumentMetaData> processedDocs = new ArrayList<DocumentMetaData>() {{
            add(new DocumentMetaData(1, "1", "1", false));
            add(new DocumentMetaData(2, "2", "2", false));
        }};
        repository.save(processedDocs);

        repository.setAsProcessed(processedDocs);

        List<DocumentMetaData> expected = new ArrayList<DocumentMetaData>() {{
            add(new DocumentMetaData(1, "1", "1", true));
            add(new DocumentMetaData(2, "2", "2", true));
        }};
        assertThat(repository.findAll()).isEqualTo(expected);
    }

    @Test
    public void testHasUnprocessedDocs() throws Exception {
        repository.save(new ArrayList<DocumentMetaData>() {{
            add(new DocumentMetaData(1, "1", "1", false));
        }});

        assertThat(repository.hasUnprocessedDocs()).isTrue();

    }
}