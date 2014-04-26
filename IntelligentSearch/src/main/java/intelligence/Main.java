package intelligence;

import infrascructure.data.email.RawEmailsRepository;
import infrascructure.data.email.crawl.EmailURLIterator;
import infrascructure.data.util.Trace;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 7/27/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String configPath = "src/main/resources/emails/nasaEmailOnlineLDAContext.xml";
        String fullPath = new File(configPath).getAbsolutePath();

        Trace.trace("Configuration path: " + fullPath);

        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(configPath);

        RawEmailsRepository rawEmailsRepository = context.getBean(RawEmailsRepository.class);
        Executors.newCachedThreadPool().submit(() -> {
            rawEmailsRepository.readAll();
            return null;
        });

        EmailURLIterator urlIterator = context.getBean(EmailURLIterator.class);
        String url;
        while ((url = urlIterator.getNextURL()) != null ){
            Trace.trace(url);
        }
    }
}
