package vagueobjects.ir.lda.tokens;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/23/14
 * Time: 5:35 PM
 * Project: IntelligentSearch
 */
public interface VocabularySerializer {
    void save(infrascructure.data.vocabulary.Vocabulary vocabulary, String path) throws IOException;
    QuickVocabulary read(String path) throws IOException;
}
