package intelligence.core.engines;

import infrascructure.data.vocabulary.Vocabulary;
import vagueobjects.ir.lda.online.demo.BatchesReader;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/4/14
 * Time: 10:35 PM
 * Project: IntelligentSearch
 */
public interface InferenceContext {
    Vocabulary getCurrentVocabulary();

    BatchesReader getBatchesReader() throws Exception;

    boolean isValid();
}
