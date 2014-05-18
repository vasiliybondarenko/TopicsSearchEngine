package infrascructure.data.email;

import infrascructure.data.dao.EmailMetaDataRepository;
import infrascructure.data.dom.email.EmailMetaData;
import infrascructure.data.util.Trace;

import javax.mail.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static infrascructure.data.email.EmailConfig.*;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/2/14
 * Time: 9:49 PM
 * Project: IntelligentSearch
 */
public class EmailReaderImpl implements EmailReader{

    private final Properties properties;
    private Folder inbox;
    private int messageCount;
    private int currentId;
    private volatile boolean emailInitialized;
    private EmailMetaDataRepository metaDataRepository;

    public EmailReaderImpl(Properties properties, EmailMetaDataRepository emailMetaDataRepository) throws IOException, MessagingException {
        this.properties = properties;
        this.metaDataRepository = emailMetaDataRepository;
    }

    @Override
    public Iterator<Message> getMessagesIterator(){
        return new Iterator<Message>() {
            @Override
            public boolean hasNext() {
                if(!emailInitialized){
                    init();
                }
                return currentId <= messageCount;
            }

            @Override
            public Message next() {
                return readNextMessage();
            }
        };
    }


    private Message readNextMessage(){
        try {
            if(!emailInitialized){
                init();
            }
            Message message = inbox.getMessage(currentId);
            metaDataRepository.save(new EmailMetaData(currentId, message.getSubject(), message.getReceivedDate(), message.getFrom()[0].toString()));
            currentId ++;
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void init() {
        try{
            String emailsDir = properties.getProperty(EMAIL_STORE_DIRECTORY);
            createSourceDirectory(emailsDir);

            Session session = Session.getInstance(properties, null);
            Store store = session.getStore();


            String host = properties.getProperty(EMAIL_HOST);
            String password = properties.getProperty(EMAIL_PASSWORD);
            String account = properties.getProperty(EMAIL_ACCOUNT);

            store.connect(host, account, password);
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            messageCount = inbox.getMessageCount();

            currentId = 1;
            List<EmailMetaData> lastItems = metaDataRepository.getLastItems(1);
            if(!lastItems.isEmpty()){
                int lastEmailId = lastItems.get(0).getId();
                if(lastEmailId > messageCount){
                    throw new IllegalStateException("LastEmailId is greater than total messages count: " + lastEmailId + " > " + messageCount);
                }
                currentId = lastEmailId + 1;
            }
            Trace.trace("EmailReader has been initialized successfully. Total messages: " + messageCount + ", current index: " + currentId);
            emailInitialized = true;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("Email box initializing failed", ex);
        }
    }

    private void createSourceDirectory(String emailsDir) throws IOException {
        Path path = Paths.get(emailsDir);
        if(!Files.exists(path)){
            Files.createDirectory(path);
        }
    }

}
