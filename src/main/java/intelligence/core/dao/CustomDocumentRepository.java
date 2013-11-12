package intelligence.core.dao;

import intelligence.core.util.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/17/13
 * Time: 11:16 PM
 * Project: IntelligentSearch
 */
@Repository
public class CustomDocumentRepository implements MyDocumentRepository{

    @Autowired
    private DocumentsRepository documentsRepository;

    @Override
    public List<Document> getDocumentsByTopic(int topic) {
        //documentsRepository.findAll(new PageRequest(1,20));
        //we should limit output!!!
        //probably we should write our own query
        return documentsRepository.findAll(new Sort(Sort.Direction.DESC, "topicsDistribution." + topic));
    }
}
