package infrascructure.data.email;

import javax.mail.Message;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/8/14
 * Time: 12:54 PM
 * Project: IntelligentSearch
 */
public interface EmailReader {
    public Iterator<Message> getMessagesIterator();
}
