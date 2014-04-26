package intelligence.core.engines;

import infrascructure.data.CrawlTaskExecutor;
import infrascructure.data.InferenceTaskExecutor;
import infrascructure.data.TaskExecutor;
import infrascructure.data.util.Trace;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 12/15/13
 * Time: 10:05 PM
 * Project: IntelligentSearch
 */
public class TaskDispatcher implements TaskExecutor{

    private final OnlineLDAContext context;
    private final CrawlTaskExecutor crawlExecutor;
    private final InferenceTaskExecutor inferenceExecutor;

    public TaskDispatcher(OnlineLDAContext context, CrawlTaskExecutor crawlExecutor, InferenceTaskExecutor inferenceExecutor) {
        this.context = context;
        this.crawlExecutor = crawlExecutor;
        this.inferenceExecutor = inferenceExecutor;
    }

    @Override
    public void start() throws Exception {
         if(!context.isValid()){
             Trace.trace("Crawling ...");
             crawlExecutor.start();
             inferenceExecutor.start();
         }else {
             Trace.trace("Online LDA started ...");
             inferenceExecutor.start();
         }
    }
}
