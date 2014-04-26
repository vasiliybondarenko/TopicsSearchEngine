package vagueobjects.ir.lda.online.demo;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/23/13
 * Time: 3:31 PM
 * Project: IntelligentSearch
 */
public class WordTuple {
    public final String word;
    public final double probability;

    public WordTuple(String word, double probability) {
        this.word = word;
        this.probability = probability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordTuple wordTuple = (WordTuple) o;

        if (Double.compare(wordTuple.probability, probability) != 0) return false;
        if (!word.equals(wordTuple.word)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = word.hashCode();
        temp = Double.doubleToLongBits(probability);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
