package intelligence.core.dao;

import infrascructure.data.dao.ResourceMetaDataRepository;
import infrascructure.data.dom.ResourceMetaData;
import infrascructure.data.dom.Tag;
import infrascructure.data.dom.Tags;
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
 * Date: 2/15/14
 * Time: 7:42 PM
 * Project: IntelligentSearch
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:localSample/test_mongodb.xml" })
public class ResourceMetaDataRepositoryTest {

    @Autowired
    private ResourceMetaDataRepository repository;

    @Before
    public void setUp() throws Exception {
        repository.deleteAll();
    }

    @After
    public void cleanDB() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void testFindById() throws Exception {
        Tag[] tags = new Tag[]{new Tag(Tags.TITLE, "")};
        ResourceMetaData resourceMetaData = new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, ""));
        repository.save(resourceMetaData);

        ResourceMetaData actual = repository.findById(1);

        assertThat(actual).isEqualTo(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "")));

    }

    @Test
    public void testFindByIdIfNoElements() throws Exception {
        Tag[] tags = new Tag[]{new Tag(Tags.TITLE, "")};
        ResourceMetaData resourceMetaData = new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, ""));
        repository.save(resourceMetaData);

        ResourceMetaData actual = repository.findById(3);

        assertThat(actual).isNull();

    }

    @Test
    public void testDeleteAll() throws Exception {
        repository.save(new ResourceMetaData(2, "A1", new Tag(Tags.TITLE, "")));

        repository.deleteAll();

        List<ResourceMetaData> metaDataList = repository.findAll();
        assertThat(metaDataList).isEmpty();
    }

    @Test
    public void testFindFirstNGreaterThanId() throws Exception {
        repository.deleteAll();
        repository.save(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "")));
        repository.save(new ResourceMetaData(2, "A2", new Tag(Tags.TITLE, "")));

        List<ResourceMetaData> list = repository.findFirstNGreaterThanId(1, 1);

        assertThat(list).hasSize(1);

    }

    @Test
    public void testSaveAll() throws Exception {
        repository.deleteAll();
        ArrayList<ResourceMetaData> resourceMetaDataList = new ArrayList<ResourceMetaData>() {{
            add(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "")));
            add(new ResourceMetaData(2, "A2", new Tag(Tags.TITLE, "")));
        }};

        repository.save(resourceMetaDataList);
        List<ResourceMetaData> actualList = repository.findAll();


        assertThat(actualList).isEqualTo(resourceMetaDataList);

    }

    @Test
    public void testCount() throws Exception {
        repository.deleteAll();
        ArrayList<ResourceMetaData> resourceMetaDataList = new ArrayList<ResourceMetaData>() {{
            add(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "")));
            add(new ResourceMetaData(2, "A2", new Tag(Tags.TITLE, "")));
        }};

        repository.save(resourceMetaDataList);
        long count = repository.count();

        assertThat(count).isEqualTo(2);

    }

    @Test
    public void testGetLastId() throws Exception {
        repository.deleteAll();
        ArrayList<ResourceMetaData> resourceMetaDataList = new ArrayList<ResourceMetaData>() {{
            add(new ResourceMetaData(2, "A1", new Tag(Tags.TITLE, "")));
            add(new ResourceMetaData(3, "A2", new Tag(Tags.TITLE, "")));
        }};

        repository.save(resourceMetaDataList);
        int actualLastId = repository.getLastId();

        assertThat(actualLastId).isEqualTo(3);

    }
}
