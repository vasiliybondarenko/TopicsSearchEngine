package infrascructure.data.email.crawl;

import infrascructure.data.Resource;
import infrascructure.data.crawl.URLIterator;
import infrascructure.data.email.RawEmailsRepository;
import infrascructure.data.email.html.EmailParser;
import infrascructure.data.email.html.NasaEmailParser;

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

    public EmailURLIterator(RawEmailsRepository emailsRepository, NasaEmailParser parser){
        this.emailsRepository = emailsRepository;
        this.parser = parser;
        currentId = 0;
    }

    @Override
    public String getNextURL() {
        Resource resource = emailsRepository.get(currentId++);
        if(resource != null){
            //PlainTextResource parsedDoc = parser.parse(resource);
        }

        return null;
    }
}
