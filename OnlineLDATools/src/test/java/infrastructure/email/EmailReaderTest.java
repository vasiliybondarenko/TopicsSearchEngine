package infrastructure.email;

import infrascructure.data.email.EmailReader;
import infrascructure.data.email.EmailReaderImpl;
import infrascructure.data.util.Trace;
import org.junit.Before;
import org.junit.Test;

import javax.mail.Message;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/13/14
 * Time: 9:34 AM
 * Project: IntelligentSearch
 */
public class EmailReaderTest {

    private EmailReader emailReader;

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/emails/email.properties"));
        emailReader = new EmailReaderImpl(properties);
    }

    @Test
    public void emailReaderShouldReadFirstEmail() throws Exception {
        Iterator<Message> messagesIterator = emailReader.getMessagesIterator();

        Message message = messagesIterator.next();

        assertThat(message).isNotNull();
        Trace.trace("Email was read: " + message.getSubject());
    }
}
