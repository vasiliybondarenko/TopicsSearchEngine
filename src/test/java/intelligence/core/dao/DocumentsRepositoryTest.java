package intelligence.core.dao;

import infrascructure.data.dom.Document;
import infrascructure.data.dom.DocumentImpl;
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
 * Date: 10/13/13
 * Time: 9:08 PM
 * Project: IntelligentSearch
 */


//@Transactional
//@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:localSample/test_mongodb.xml" })
public class DocumentsRepositoryTest {

    @Autowired
    private DocumentsRepository repository;

    @Test
    public void testSaveDocuments() throws Exception {
        DocumentImpl document = new DocumentImpl(2, "Some kind of monster", new double[]{1, 2, 3});
        repository.deleteAll();

        repository.save(document);

        assertThat(repository.findAll()).isNotEmpty();
    }

    @Test
    public void testDocumentsByTopics() throws Exception {
        List<Document> documents = new ArrayList<Document>(){{
            add(new DocumentImpl(1, "Some kind of monster", new double[]{1, 2, 3}));
            add(new DocumentImpl(2, "Some kind of monster", new double[]{2, 3, 1}));
            add(new DocumentImpl(3, "Some kind of monster", new double[]{3, 1, 2}));
        }};
        repository.deleteAll();
        repository.save(documents);
        List<Document> expected = new ArrayList<Document>(){{
            add(new DocumentImpl(3, "Some kind of monster", new double[]{3, 1, 2}));
            add(new DocumentImpl(2, "Some kind of monster", new double[]{2, 3, 1}));
            add(new DocumentImpl(1, "Some kind of monster", new double[]{1, 2, 3}));
        }};

        List<Document> foundDocuments = repository.getDocumentsByTopic(0, 0);

        assertThat(foundDocuments).isEqualTo(expected);

    }

    @Test
    public void testDocumentsByTopicsLimit() throws Exception {
        List<Document> documents = new ArrayList<Document>(){{
            add(new DocumentImpl(1, "Some kind of monster", new double[]{1, 2, 3}));
            add(new DocumentImpl(2, "Some kind of monster", new double[]{2, 3, 1}));
            add(new DocumentImpl(3, "Some kind of monster", new double[]{3, 1, 2}));
            add(new DocumentImpl(3, "Some kind of monster", new double[]{4, 3, 2}));
        }};
        repository.deleteAll();
        repository.save(documents);
        int limit = 3;

        List<Document> foundDocuments = repository.getDocumentsByTopic(0, limit);

        assertThat(foundDocuments).hasSize(limit);

    }
}
