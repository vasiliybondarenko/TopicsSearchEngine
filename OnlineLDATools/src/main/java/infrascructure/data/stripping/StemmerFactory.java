package infrascructure.data.stripping;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/26/14
 * Time: 9:45 PM
 * Project: IntelligentSearch
 */
public class StemmerFactory {
    public static Stemmer createStemmer(){
        return new EnglishSuffixStripper();
    }
}
