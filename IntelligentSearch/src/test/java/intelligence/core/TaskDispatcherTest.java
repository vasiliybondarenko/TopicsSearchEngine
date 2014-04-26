package intelligence.core;

import infrascructure.data.CrawlTaskExecutor;
import infrascructure.data.InferenceTaskExecutor;
import infrascructure.data.TaskExecutor;
import intelligence.core.engines.OnlineLDAContext;
import intelligence.core.engines.TaskDispatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 12/29/13
 * Time: 10:19 PM
 * Project: IntelligentSearch
 */

@RunWith(MockitoJUnitRunner.class)
public class TaskDispatcherTest {

    @Mock
    private OnlineLDAContext context;

    @Mock
    private CrawlTaskExecutor crawler;

    @Mock
    private InferenceTaskExecutor onlineLdaExecutor;

    @Autowired
    private TaskDispatcher taskDispatcher;

    @Test
    public void crawlerShouldStartIfContextInvalid() throws Exception {
        TaskExecutor taskDispatcher = new TaskDispatcher(context, crawler, onlineLdaExecutor);
        when(context.isValid()).thenReturn(false);

        taskDispatcher.start();

        verify(crawler).start();
    }

    @Test
    public void onlineLDAShouldStartIfContextValid() throws Exception {
        TaskExecutor taskDispatcher = new TaskDispatcher(context, crawler, onlineLdaExecutor);
        when(context.isValid()).thenReturn(true);

        taskDispatcher.start();

        verify(onlineLdaExecutor).start();
    }

    @Test
    public void onlineLDAShouldStartAfterCrawlingCompleted() throws Exception {
        TaskExecutor taskDispatcher = new TaskDispatcher(context, crawler, onlineLdaExecutor);
        when(context.isValid()).thenReturn(false);

        taskDispatcher.start();

        InOrder inOrder = inOrder(crawler, onlineLdaExecutor);
        inOrder.verify(crawler).start();
        inOrder.verify(onlineLdaExecutor).start();
    }

}
