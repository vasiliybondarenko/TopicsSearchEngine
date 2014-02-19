package infrascructure.data.email;

import javax.mail.Address;
import javax.mail.Message;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/19/14
 * Time: 10:10 PM
 * Project: IntelligentSearch
 */
public class CodeProjectEmailFilter implements EmailFilter {
    @Override
    public boolean accept(Message email) {
        try {
            Address[] from = email.getFrom();
            return from[0].toString().toLowerCase().contains("codeproject.com");
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
