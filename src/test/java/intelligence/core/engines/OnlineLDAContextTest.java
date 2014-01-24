package intelligence.core.engines;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import vagueobjects.ir.lda.online.demo.BatchesReader;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/10/14
 * Time: 9:59 PM
 * Project: IntelligentSearch
 */

@RunWith(MockitoJUnitRunner.class)
public class OnlineLDAContextTest {

    @Mock
    private BatchesReader batchReader;

    @Test
    public void contextShouldBeValidIfNextBatchIsAvailable() throws Exception {
        OnlineLDAContext context = new OnlineLDAContext(batchReader);
        when(batchReader.hasNextBatch()).thenReturn(true);

        assertThat(context.isValid()).isTrue();
    }
}
