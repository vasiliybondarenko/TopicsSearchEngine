package intelligence.core
import infrascructure.data.CrawlTaskExecutor
import infrascructure.data.InferenceTaskExecutor
import intelligence.core.engines.OnlineLDAContext
import intelligence.core.engines.TaskDispatcher
import spock.lang.Specification
/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 6/1/14
 * Time: 8:00 PM 
 * Project: NewTopicSearch
 */
class TaskDispatcherTest extends Specification{

    def context = Mock(OnlineLDAContext)
    def crawler = Mock(CrawlTaskExecutor)
    def onlineLdaExecutor = Mock(InferenceTaskExecutor)

    def "crawler should start if context is invalid"(){
        setup:
        def taskDispatcher = new TaskDispatcher(context, crawler, onlineLdaExecutor)
        context.isValid() >> false

        when: taskDispatcher.start()
        then: 1 * crawler.start()
    }

    def "OnlineLDA inference should start if context is valid"(){
        setup:
        def taskDispatcher = new TaskDispatcher(context, crawler, onlineLdaExecutor)
        context.isValid() >> true

        when: taskDispatcher.start()
        then: 1 * onlineLdaExecutor.start()
    }

    def "OnlineLDA should start after crawling has been completed"(){
        setup:
        def taskDispatcher = new TaskDispatcher(context, crawler, onlineLdaExecutor)
        context.isValid() >> false

        when: taskDispatcher.start()

        then: 1 * crawler.start()
        then: 1 * onlineLdaExecutor.start()
    }
}
