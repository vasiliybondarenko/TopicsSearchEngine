package vagueobjects.ir.lda.online.demo;

import infrascructure.data.util.CloseableWriter;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/23/13
 * Time: 5:57 PM
 * Project: IntelligentSearch
 */
public interface OnlineLDAResultWriter {
    void writeTopWords(OnlineLDAResult onlineLDAResult, CloseableWriter writer, int topWordsCount);
    void writeDocumentTopicsDistribution(OnlineLDAResult onlineLDAResult, CloseableWriter writer);
}
