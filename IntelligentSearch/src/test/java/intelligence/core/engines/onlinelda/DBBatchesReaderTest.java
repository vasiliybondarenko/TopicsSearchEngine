package intelligence.core.engines.onlinelda;

import infrascructure.data.dom.DocumentMetaData;
import infrascructure.data.util.IOHelper;
import intelligence.core.dao.DocumentMetaDataRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import vagueobjects.ir.lda.online.demo.DocumentData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/10/14
 * Time: 10:45 PM
 * Project: IntelligentSearch
 */
@RunWith(MockitoJUnitRunner.class)
public class DBBatchesReaderTest {

    @Mock
    private DocumentMetaDataRepository repository;

    private final ArrayList<DocumentMetaData> metaData = new ArrayList<DocumentMetaData>() {{
        add(new DocumentMetaData(1, "T1", "a1.txt", false));
        add(new DocumentMetaData(2, "T2", "a2.txt", false));
    }};

    private final ArrayList<DocumentData> docs = new ArrayList<DocumentData>() {{
        add(new DocumentData(1, "T1", "Text1\nText1\n"));
        add(new DocumentData(2, "T2", "Text2\nText2\n"));
    }};

    @Before
    public void setUp() throws Exception {
        saveDocs(metaData, docs);
    }

    @After
    public void cleanUp() throws Exception {
        removeDocs(metaData);
    }

    @Test
    public void getNextBatchShouldReturnEmptyIteratorIfNoBatches() throws Exception {
        DBBatchesReader batchesReader = new DBBatchesReader(2, repository);
        when(repository.getUnprocessedDocs(2)).thenReturn(new ArrayList<>());

        assertThat(batchesReader.getNextBatch()).isEmpty();
    }

    @Test
    public void getNextBatchShouldReadNextBatchFromDB() throws Exception {
        int batchSize = metaData.size();
        DBBatchesReader batchesReader = new DBBatchesReader(batchSize, repository);
        when(repository.getUnprocessedDocs(batchSize)).thenReturn(metaData);

        List<DocumentData> nextBatchRead = batchesReader.getNextBatch();

        assertThat(nextBatchRead).isEqualTo(docs);
    }

    @Test
    public void batchShouldBeMarkedAsProcessedAfterProcessingCompleted() throws Exception {
        int batchSize = metaData.size();
        DBBatchesReader batchesReader = new DBBatchesReader(batchSize, repository);
        when(repository.getUnprocessedDocs(batchSize)).thenReturn(metaData);

        batchesReader.getNextBatch();
        batchesReader.getNextBatch();

        verify(repository).setAsProcessed(metaData);
    }

    @Test
    public void batchesReaderShouldNotHaveNextBatchIfNoUnprocessedBatches() throws Exception {
        DBBatchesReader batchesReader = new DBBatchesReader(2, repository);
        when(repository.getUnprocessedDocs(2)).thenReturn(new ArrayList<>());

        assertThat(batchesReader.hasNextBatch()).isFalse();
    }

    @Test
    public void batchesReaderShouldHaveNextBatchIfNextUnprocessedBatchAvailable() throws Exception {
        DBBatchesReader batchesReader = new DBBatchesReader(2, repository);
        when(repository.getUnprocessedDocs(2)).thenReturn(new ArrayList<DocumentMetaData>(){{
            add(new DocumentMetaData(1, "", "", false));
        }});

        assertThat(batchesReader.hasNextBatch()).isTrue();
    }

    private void saveDocs(List<DocumentMetaData> documentMetaData, List<DocumentData> docs) throws IOException {
        for(int i = 0; i < documentMetaData.size(); i ++){
            String path = documentMetaData.get(i).getFilePath();
            String text = docs.get(i).getText();
            IOHelper.saveToFile(path, text);
        }
    }

    private void removeDocs(List<DocumentMetaData> metaData){
        for (int i = 0; i < metaData.size(); i++) {
            new File(metaData.get(i).getFilePath()).delete();
        }
    }
}
