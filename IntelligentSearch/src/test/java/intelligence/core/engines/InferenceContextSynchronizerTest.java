package intelligence.core.engines;

import infrascructure.data.dom.DocumentMetaData;
import infrascructure.data.integration.DirectoryDocumentMetaDataReader;
import intelligence.core.dao.DocumentMetaDataRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/19/14
 * Time: 10:58 PM
 * Project: IntelligentSearch
 */

@RunWith(MockitoJUnitRunner.class)
public class InferenceContextSynchronizerTest {

    @Mock
    private DocumentMetaDataRepository repository;

    @Mock
    private DirectoryDocumentMetaDataReader documentMetaDataReader;

    @Test
    public void contextShouldSynchronizeFromCrawlerFolder() throws Exception {
        InferenceContextSynchronizer synchronizer = new FileToDBInferenceContextSynchronizer(repository, documentMetaDataReader);
        when(documentMetaDataReader.readDocumentMetaData()).thenReturn(new ArrayList<DocumentMetaData>(){{
            add(new DocumentMetaData(1, "T1", "P1", false));
            add(new DocumentMetaData(2, "T2", "P2", false));
        }}.iterator());

        synchronizer.synchronize();

        InOrder inOrder = inOrder(documentMetaDataReader, repository);
        inOrder.verify(documentMetaDataReader).readDocumentMetaData();
        inOrder.verify(repository).save(new DocumentMetaData(1, "T1", "P1", false));
        inOrder.verify(repository).save(new DocumentMetaData(2, "T2", "P2", false));
    }

    @Test
    public void directoryReaderShouldBeClosedAfterCompleting() throws Exception {
        InferenceContextSynchronizer synchronizer = new FileToDBInferenceContextSynchronizer(repository, documentMetaDataReader);
        when(documentMetaDataReader.readDocumentMetaData()).thenReturn(new ArrayList<DocumentMetaData>().iterator());
        when(repository.save(new DocumentMetaData(-1, "", "", false))).thenThrow(new RuntimeException());

        synchronizer.synchronize();

        verify(documentMetaDataReader).close();
    }
}
