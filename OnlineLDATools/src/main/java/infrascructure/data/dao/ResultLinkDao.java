package infrascructure.data.dao;

import infrascructure.data.email.html.entity.ResultLink;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/9/14
 * Time: 10:15 AM
 * Project: IntelligentSearch
 */
public interface ResultLinkDao extends MongoRepository<ResultLink, String> {

    ResultLink findByrawDocId(String rawDocId);

    ResultLink findByUrl(String url1);
}
