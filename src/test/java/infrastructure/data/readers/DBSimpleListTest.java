package infrastructure.data.readers;

import infrascructure.data.dao.ResourceMetaDataRepository;
import infrascructure.data.dom.ResourceMetaData;
import infrascructure.data.dom.Tag;
import infrascructure.data.dom.Tags;
import infrascructure.data.readers.DBSimpleList;
import infrascructure.data.serialize.SimpleResourceSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/15/14
 * Time: 9:49 PM
 * Project: IntelligentSearch
 */

@RunWith(MockitoJUnitRunner.class)
public class DBSimpleListTest {

    @Mock
    private ResourceMetaDataRepository repository;

    @Mock
    private SimpleResourceSerializer serializer;

    @Test
    public void emptyListShouldHaveSizeZero() throws Exception {
        DBSimpleList list = new DBSimpleList(repository, serializer, 1);

        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    public void listShouldHaveSize1AfterOneElementAdded() throws Exception {
        DBSimpleList list = new DBSimpleList(repository, serializer, 1);

        list.add(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));

        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void listShouldContainElementThatWasAdded() throws Exception {
        DBSimpleList list = new DBSimpleList(repository, serializer, 1);

        list.add(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));

        assertThat(list.contains(1)).isTrue();
    }

    @Test
    public void emptyListShouldNotContainAnyElements() throws Exception {
        DBSimpleList list = new DBSimpleList(repository, serializer, 1);

        assertThat(list.contains(1)).isFalse();
    }

    @Test
    public void listShouldSaveAllElementsBufferOverflowed() throws Exception {
        DBSimpleList list = new DBSimpleList(repository, serializer, 1);
        List<ResourceMetaData> listToSave = new ArrayList<ResourceMetaData>(){{
            add(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));
        }};

        list.add(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));
        list.add(new ResourceMetaData(2, "A2", new Tag(Tags.TITLE, "TT")));

        verify(repository).save(listToSave);
    }

    @Test
    public void listShouldContainAllElementsAfterBufferCleared() throws Exception {
        DBSimpleList list = new DBSimpleList(repository, serializer, 1);

        list.add(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));
        list.add(new ResourceMetaData(2, "A2", new Tag(Tags.TITLE, "TT")));
        when(repository.findById(1)).thenReturn(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));
        when(repository.findById(2)).thenReturn(new ResourceMetaData(2, "A2", new Tag(Tags.TITLE, "TT")));

        assertThat(list.contains(1)).isTrue();
        assertThat(list.contains(2)).isTrue();
    }

    @Test
    public void listShouldHaveSize2AfterBufferCleared() throws Exception {
        DBSimpleList list = new DBSimpleList(repository, serializer, 1);

        list.add(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));
        list.add(new ResourceMetaData(2, "A2", new Tag(Tags.TITLE, "TT")));

        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void initialSizeShouldBeObtainedFromDb() throws Exception {
        DBSimpleList list1 = new DBSimpleList(repository, serializer, 1);
        when(repository.count()).thenReturn((long)1);

        list1.add(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));
        list1.add(new ResourceMetaData(2, "A2", new Tag(Tags.TITLE, "TT")));
        DBSimpleList list2 = new DBSimpleList(repository, serializer, 1);


        assertThat(list2.size()).isEqualTo(1);
    }

    @Test
    public void getShouldReturnElementAfterListReload() throws Exception {
        DBSimpleList list1 = new DBSimpleList(repository, serializer, 1);
        when(repository.findById(1)).thenReturn(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));

        list1.add(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));
        list1.add(new ResourceMetaData(2, "A2", new Tag(Tags.TITLE, "TT")));
        DBSimpleList reInitializedList = new DBSimpleList(repository, serializer, 1);


        assertThat(reInitializedList.get(1)).isEqualTo(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));
    }

    @Test(timeout = 5000)
    public void getShouldBeBlockedIfElementIsAbsent() throws Exception {
        DBSimpleList list = new DBSimpleList(repository, serializer, 1);
        when(repository.findById(1)).thenReturn(null);
        ExecutorService executor = Executors.newCachedThreadPool();

        Future<ResourceMetaData> future = executor.submit(() -> list.get(1));
        boolean timeOutExpired = false;
        try {
            future.get(1000, TimeUnit.MILLISECONDS);
        }catch (TimeoutException e){
            timeOutExpired = true;
        }

        assertThat(timeOutExpired).overridingErrorMessage("'get' method should block current thread when there is no element with such id").isTrue();
    }

    @Test(timeout = 5000)
    public void getShouldWaitForNewElements() throws Exception {
        DBSimpleList list = new DBSimpleList(repository, serializer, 1);
        when(repository.findById(1)).thenReturn(null);
        ExecutorService executor = Executors.newCachedThreadPool();

        Future<ResourceMetaData> future = executor.submit(() -> list.get(1));
        Executors.newCachedThreadPool().submit(() -> {
            try {
                list.add(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ResourceMetaData resourceMetaData = future.get();

        assertThat(resourceMetaData).isEqualTo(new ResourceMetaData(1, "A1", new Tag(Tags.TITLE, "T1")));
    }

    @Test(timeout = 1000)
    public void getShouldReturnNullForGapId() throws Exception {
        DBSimpleList list = new DBSimpleList(repository, serializer, 1);
        when(repository.findById(1)).thenReturn(null);
        when(repository.findById(2)).thenReturn(new ResourceMetaData(2, "A2", new Tag(Tags.TITLE, "T2")));

        list.add(new ResourceMetaData(2, "A2", new Tag(Tags.TITLE, "T2")));
        ResourceMetaData actualFoundDoc = list.get(1);

        assertThat(actualFoundDoc).overridingErrorMessage(
                "'get' method should return null for non-existent id if there are docs with id > requested id "
        ).isNull();

    }
}
