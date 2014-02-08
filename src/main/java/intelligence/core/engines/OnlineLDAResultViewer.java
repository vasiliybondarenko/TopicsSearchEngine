package intelligence.core.engines;

import infrascructure.data.util.Trace;
import intelligence.core.dao.DocumentsRepository;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/9/14
 * Time: 1:15 AM
 * Project: IntelligentSearch
 */
public class OnlineLDAResultViewer {
    public static void main(String[] args) {
        String configPath = "src/main/resources/emails/nasaEmailOnlineLDAContext.xml";

        String fullPath = new File(configPath).getAbsolutePath();

        Trace.trace("Configuration path: " + fullPath);

        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(configPath);

        DocumentsRepository documentsRepository = context.getBean(DocumentsRepository.class);
        documentsRepository.getDocumentsByTopic(0, 200).forEach((d) -> System.out.println(d.getTitle() + d.getTopicsDistribution()[0]));
    }
}
