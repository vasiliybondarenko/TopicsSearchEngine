package infrascructure.data.email;

import javax.mail.Message;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/8/14
 * Time: 4:13 PM
 * Project: IntelligentSearch
 */
public interface EmailFilter {
    public boolean accept(Message email);
}
