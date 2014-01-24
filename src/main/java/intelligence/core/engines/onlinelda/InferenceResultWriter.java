package intelligence.core.engines.onlinelda;

import infrascructure.data.dom.Document;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/24/14
 * Time: 8:00 PM
 * Project: IntelligentSearch
 */
public interface InferenceResultWriter {
    void saveDocumentsDistribution(Iterable<Document> documents);
}
