package infrascructure.data.stripping;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 8/29/13
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Stemmer {
    String getCanonicalForm(String word);
}
