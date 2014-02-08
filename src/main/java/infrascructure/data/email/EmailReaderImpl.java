package infrascructure.data.email;

import infrascructure.data.util.Trace;

import javax.mail.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Properties;

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

    public EmailReaderImpl(Properties properties) throws IOException, MessagingException {
        this.properties = properties;
        init();
    }

    public Iterator<Message> getMessagesIterator(){
        return new Iterator<Message>() {
            @Override
            public boolean hasNext() {
                return currentId > 1;
            }

            @Override
            public Message next() {
                return readNextMessage();
            }
        };
    }


    private Message readNextMessage(){
        try {
            return inbox.getMessage(--currentId);
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void init() throws IOException, MessagingException {
        String emailsDir = properties.getProperty("mail.store_directory");
        createSourceDirectory(emailsDir);

        Session session = Session.getInstance(properties, null);
        Store store = session.getStore();

        String password = properties.getProperty("mail.password");
        String account = properties.getProperty("mail.account");

        store.connect("imap.gmail.com", account, password);
        inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        messageCount = inbox.getMessageCount();
        currentId = messageCount - 1;
    }

    private void createSourceDirectory(String emailsDir) throws IOException {
        Path path = Paths.get(emailsDir);
        if(!Files.exists(path)){
            Files.createDirectory(path);
        }
    }

    public static void main(String[] args) throws IOException, MessagingException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/emails/email.properties"));
        EmailReaderImpl emailReader = new EmailReaderImpl(properties);
        Iterator<Message> messagesIterator = emailReader.getMessagesIterator();
        while (messagesIterator.hasNext()){
            Message message = messagesIterator.next();
            Trace.trace(message.getSubject());
        }

    }
}
