package intelligence.core.dao;

import infrascructure.data.dom.DocumentMetaData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/11/14
 * Time: 4:27 PM
 * Project: IntelligentSearch
 */
@Repository
public interface DocumentMetaDataRepository extends MongoRepository<DocumentMetaData, Integer>, CustomDocumentMetaDataRepository{
}
