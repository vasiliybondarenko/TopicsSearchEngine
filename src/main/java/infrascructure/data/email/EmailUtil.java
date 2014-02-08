package infrascructure.data.email;

import infrascructure.data.util.Trace;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/8/14
 * Time: 2:46 PM
 * Project: IntelligentSearch
 */
public class EmailUtil {
    public static String getEmail(Message message) throws IOException, MessagingException {
        Object content = message.getContent();
        StringBuilder sb = new StringBuilder("");
        if(content instanceof Multipart){
            Multipart multyPart = (Multipart)message.getContent();
            int partsCount = multyPart.getCount();
            for(int p = 0; p < partsCount; p ++){
                BodyPart part = multyPart.getBodyPart(p);
                sb.append(part.getContent().toString() + "\n");
            }
            return sb.toString();
        }else{
            Trace.trace(content.getClass() + " is not supported yet");
        }
        return null;
    }

}
