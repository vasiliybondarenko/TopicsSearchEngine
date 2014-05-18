package infrascructure.data.email.crawl;

import infrascructure.data.Resource;
import infrascructure.data.crawl.URLIterator;
import infrascructure.data.dao.ResultLinkDao;
import infrascructure.data.email.RawEmailsRepository;
import infrascructure.data.email.html.EmailParser;
import infrascructure.data.email.html.entity.ResultLink;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

    private Queue<ResultLink> resultLinks;

    private EmailParser parser;

    private int currentId;

    @Autowired
    private ResultLinkDao resultLinkDao;

    @Autowired
    public EmailURLIterator(RawEmailsRepository emailsRepository, EmailParser parser){
        this.emailsRepository = emailsRepository;
        this.parser = parser;
        currentId = 0;
        this.resultLinks = new LinkedList<>();
        readEmailsAsync();
    }

    @Override
    public String getNextURL() {
        if(!resultLinks.isEmpty()){
            return resultLinks.poll().getUrl();
        }
        Resource resource = emailsRepository.get(currentId++);
        if(resource != null){
            try {
                List<ResultLink> links = parser.parse(resource.getData());
                resultLinks.addAll(links);
                resultLinkDao.save(links);
                return !resultLinks.isEmpty() ? resultLinks.poll().getUrl() : null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void readEmailsAsync(){
        Executors.newCachedThreadPool().submit(()->{
            try{
                emailsRepository.readAll();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        });
    }
}
