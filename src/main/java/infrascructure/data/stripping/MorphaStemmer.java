package infrascructure.data.stripping;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/24/14
 * Time: 8:39 PM
 * Project: IntelligentSearch
 */
public class MorphaStemmer implements Stemmer {
    private final edu.washington.cs.knowitall.morpha.MorphaStemmer morphaStemmer;

    public MorphaStemmer() {
        this.morphaStemmer = new edu.washington.cs.knowitall.morpha.MorphaStemmer();
    }

    @Override
    public String getCanonicalForm(String word) {
        return morphaStemmer.morpha(word);
    }
}
