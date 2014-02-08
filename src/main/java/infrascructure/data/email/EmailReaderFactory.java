package infrascructure.data.email;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/8/14
 * Time: 12:55 PM
 * Project: IntelligentSearch
 */
public abstract class EmailReaderFactory {
    abstract EmailReader createEmailReader()  throws Exception;
}
