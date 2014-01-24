package intelligence.core.engines;

import infrascructure.data.vocabulary.Vocabulary;
import vagueobjects.ir.lda.online.demo.BatchesReader;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 12/29/13
 * Time: 9:35 PM
 * Project: IntelligentSearch
 */
public class OnlineLDAContext implements InferenceContext{

    private final BatchesReader batchesReader;

    public OnlineLDAContext(BatchesReader batchesReader) {
        this.batchesReader = batchesReader;
    }

    @Override
    public Vocabulary getCurrentVocabulary() {
        return null;
    }

    @Override
    public BatchesReader getBatchesReader() throws Exception {
        return batchesReader;
    }

    @Override
    public boolean isValid(){
        return batchesReader.hasNextBatch();
    }
}
