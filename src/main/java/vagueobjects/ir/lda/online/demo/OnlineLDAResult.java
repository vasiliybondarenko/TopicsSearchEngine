package vagueobjects.ir.lda.online.demo;

import infrascructure.data.dom.Document;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/22/13
 * Time: 10:00 PM
 * Project: IntelligentSearch
 */
public interface OnlineLDAResult {
    /**
     * Number of terms per each tokens to show
     */
    int NUMBER_OF_TOKENS = 50;

    List<Document> getDocuments();

    List<Topic> getTopics();
}
