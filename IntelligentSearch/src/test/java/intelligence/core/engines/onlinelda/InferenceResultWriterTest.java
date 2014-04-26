package intelligence.core.engines.onlinelda;

import infrascructure.data.dom.Document;
import intelligence.core.dao.DocumentsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;


/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/24/14
 * Time: 8:01 PM
 * Project: IntelligentSearch
 */
@RunWith(MockitoJUnitRunner.class)
public class InferenceResultWriterTest {

    @Mock
    private DocumentsRepository documentsRepository;

    @Test
    public void resultsWriterShouldSaveDocsDistribution() throws Exception {
        InferenceResultWriter resultWriter = new OnlineLDAResultsWriter(documentsRepository);

        resultWriter.saveDocumentsDistribution(new ArrayList<>());

        verify(documentsRepository).save(new ArrayList<Document>());
    }
}
