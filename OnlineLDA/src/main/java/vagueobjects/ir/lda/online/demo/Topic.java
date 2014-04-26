package vagueobjects.ir.lda.online.demo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/22/13
 * Time: 9:59 PM
 * Project: IntelligentSearch
 */
public class Topic {
    public final List<WordTuple> words;

    public Topic(List<WordTuple> words) {
        this.words = words;
    }
}
