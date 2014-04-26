package infrascructure.data.stripping;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/6/13
 * Time: 9:59 PM
 * Project: IntelligentSearch
 */
public class VoidStripper implements Stemmer{
    @Override
    public String getCanonicalForm(String word) {
        return word;
    }
}
