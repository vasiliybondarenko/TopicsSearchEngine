package infrascructure.data.email;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/8/14
 * Time: 4:16 PM
 * Project: IntelligentSearch
 */
public class NasaEmailFilter implements EmailFilter {
    @Override
    public boolean accept(Message email) {
        try {
            Address[] from = email.getFrom();
            return from[0].toString().toLowerCase().contains("nasa_subscriptions@service.govdelivery.com");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
