package intelligence.core;

import intelligence.core.dao.DocumentsRepository;
import intelligence.core.dao.MyDocumentRepository;
import intelligence.core.util.Document;
import intelligence.core.util.DocumentImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
@ContextConfiguration(locations = { "classpath:mongodb.xml" })
public class DocumentsRepositoryTest {

    @Autowired
    private DocumentsRepository repository;

    @Autowired
    private MyDocumentRepository myDocumentRepository;

    @Test
    public void testSaveDocuments() throws Exception {
        DocumentImpl document = new DocumentImpl(2, "Some kind of monster", new double[]{1, 2, 3});
        repository.deleteAll();

        repository.save(document);

        assertThat(repository.findAll()).isNotEmpty();
    }

    @Test
    public void testGetDocumentsByTopic() throws Exception {
        repository.deleteAll();
        List<Document> documents = new ArrayList<>(3);
        documents.add(new DocumentImpl(1, "Some kind of monster", new double[]{2, 3, 0}));
        documents.add(new DocumentImpl(2, "Some kind of monster", new double[]{1, 1, 0}));
        documents.add(new DocumentImpl(3, "Some kind of monster", new double[]{3, 2, 0}));
        List<Document> expectedDocs = new ArrayList<>();
        expectedDocs.add(new DocumentImpl(3, "Some kind of monster", new double[]{3, 2, 0}));
        expectedDocs.add(new DocumentImpl(1, "Some kind of monster", new double[]{2, 3, 0}));
        expectedDocs.add(new DocumentImpl(2, "Some kind of monster", new double[]{1, 1, 0}));

        repository.save(documents);
        List<Document> foundDocuments = repository.getDocumentsByTopic(0);

        assertThat(foundDocuments).isEqualTo(expectedDocs);


    }

    @Test
    public void testFindAllSortedByTopic() throws Exception {
        repository.deleteAll();
        List<Document> documents = new ArrayList<>(3);
        documents.add(new DocumentImpl(1, "Some kind of monster", new double[]{2, 3, 0}));
        documents.add(new DocumentImpl(2, "Some kind of monster", new double[]{1, 1, 0}));
        documents.add(new DocumentImpl(3, "Some kind of monster", new double[]{3, 2, 0}));
        List<Document> expectedDocs = new ArrayList<>();
        expectedDocs.add(new DocumentImpl(3, "Some kind of monster", new double[]{3, 2, 0}));
        expectedDocs.add(new DocumentImpl(1, "Some kind of monster", new double[]{2, 3, 0}));
        expectedDocs.add(new DocumentImpl(2, "Some kind of monster", new double[]{1, 1, 0}));

        repository.save(documents);
        List<Document> foundDocuments = repository.findAll(new Sort(Sort.Direction.DESC, "topicsDistribution.0"));

        assertThat(foundDocuments).isEqualTo(expectedDocs);

    }

    @Test
    public void testMyDocumentRepository() throws Exception {
        DocumentImpl document = new DocumentImpl(2, "Some kind of monster", new double[]{1, 2, 3});
        List<Document> documents = new ArrayList<>();
        documents.add(document);
        repository.deleteAll();
        repository.save(documents);

        List<Document> foundDocuments = myDocumentRepository.getDocumentsByTopic(0);

        assertThat(foundDocuments).isEqualTo(documents);



    }
}
