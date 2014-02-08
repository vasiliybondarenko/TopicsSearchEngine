package infrascructure.data.email.crawl;

import infrascructure.data.Resource;
import infrascructure.data.crawl.URLIterator;
import infrascructure.data.email.RawEmailsRepository;
import infrascructure.data.email.html.EmailParser;
import infrascructure.data.email.html.NasaEmailParser;
import infrascructure.data.email.html.entity.ResultLink;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/8/14
 * Time: 5:24 PM
 * Project: IntelligentSearch
 */
public class EmailURLIterator implements URLIterator{

    private RawEmailsRepository emailsRepository;

    private EmailParser parser;

    private int currentId;

    @Autowired
    public EmailURLIterator(RawEmailsRepository emailsRepository, NasaEmailParser parser){
        this.emailsRepository = emailsRepository;
        this.parser = parser;
        currentId = 0;
        readEmailsAsync();
    }

    @Override
    public String getNextURL() {
        Resource resource = emailsRepository.get(currentId++);
        if(resource != null){
            try {
                ResultLink resultLink = parser.parse(resource.getData());
                return resultLink.getUrl();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void readEmailsAsync(){
        Executors.newCachedThreadPool().submit(()->{
            emailsRepository.readAll();
            return null;
        });
    }
}
