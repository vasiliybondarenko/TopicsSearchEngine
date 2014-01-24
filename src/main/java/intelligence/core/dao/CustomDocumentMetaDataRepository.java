package intelligence.core.dao;

import infrascructure.data.dom.DocumentMetaData;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/11/14
 * Time: 5:37 PM
 * Project: IntelligentSearch
 */
public interface CustomDocumentMetaDataRepository {
    List<DocumentMetaData> getUnprocessedDocs(int batchSize);
    void setAsProcessed(List<DocumentMetaData> processedDocs);
    boolean hasUnprocessedDocs();
}
