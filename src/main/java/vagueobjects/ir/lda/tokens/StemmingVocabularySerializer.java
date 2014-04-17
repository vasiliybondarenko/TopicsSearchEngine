package vagueobjects.ir.lda.tokens;

import infrascructure.data.util.IOHelper;
import infrascructure.data.vocabulary.Vocabulary;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/23/14
 * Time: 5:38 PM
 * Project: IntelligentSearch
 */
public class StemmingVocabularySerializer implements VocabularySerializer {
    @Override
    public void save(Vocabulary vocabulary, String path) throws IOException {
        IOHelper.writeLinesToFile(path, vocabulary.getWords().keySet());
    }

    @Override
    public QuickVocabulary read(String path) throws IOException {
        List<String> words = IOHelper.readLinesFromFile(path);
        return new QuickVocabulary(words);
    }
}
