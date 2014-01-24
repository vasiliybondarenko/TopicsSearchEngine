package intelligence.core.engines;

import infrascructure.data.util.Trace;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/4/14
 * Time: 9:13 PM
 * Project: IntelligentSearch
 */
public class TasksRunner {
    public static void main(String[] args) throws Exception {
        String configPath = "src/main/resources/localSampleContext.xml";
        String fullPath = new File(configPath).getAbsolutePath();

        Trace.trace("Configuration path: " + fullPath);

        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(configPath);
        TaskDispatcher taskDispatcher = context.getBean(TaskDispatcher.class);
        taskDispatcher.start();
    }
}
