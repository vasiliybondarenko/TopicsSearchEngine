package intelligence.core.engines.crawlers;

import infrascructure.data.BaseCrawlerLauncher;
import infrascructure.data.util.Trace;
import intelligence.core.engines.InferenceContextSynchronizer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 12/28/13
 * Time: 10:30 PM
 * Project: IntelligentSearch
 */
public class CrawlerLauncher extends BaseCrawlerLauncher {

    private final InferenceContextSynchronizer contextSynchronizer;

    @Autowired
    public CrawlerLauncher(InferenceContextSynchronizer contextSynchronizer) {
        this.contextSynchronizer = contextSynchronizer;
    }

    @Override
    protected void afterCrawlingCompleted(Future<Throwable> parseResult) throws Exception {
        Trace.trace("Building vocabulary and synchronizing db...");
        buildVocabulary(parseResult);
        contextSynchronizer.synchronize();
    }
}
