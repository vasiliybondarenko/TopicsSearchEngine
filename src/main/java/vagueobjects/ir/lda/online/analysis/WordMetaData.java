package vagueobjects.ir.lda.online.analysis;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/31/14
 * Time: 9:31 PM
 * Project: IntelligentSearch
 */
public class WordMetaData implements Comparable<WordMetaData>{
    public final String word;
    public final double TF_IDF;

    public WordMetaData(String word, double tf_idf) {
        this.word = word;
        TF_IDF = tf_idf;
    }

    @Override
    public int compareTo(WordMetaData o) {
        return Double.compare(o.TF_IDF, this.TF_IDF);
    }

    @Override
    public String toString() {
        return word + "\t" + String.format("%.3f", TF_IDF);
    }
}
