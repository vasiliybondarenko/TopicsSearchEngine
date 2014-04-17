package vagueobjects.ir.lda.tokens;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/23/14
 * Time: 6:23 PM
 * Project: IntelligentSearch
 */
public class VocabularySerializerFactory {
    public static VocabularySerializer createVocabularySerializer(){
        return new StemmingVocabularySerializer();
    }
}
