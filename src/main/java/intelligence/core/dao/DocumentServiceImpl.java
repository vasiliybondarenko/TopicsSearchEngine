package intelligence.core.dao;

import intelligence.core.DocumentService;
import intelligence.core.util.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/18/13
 * Time: 12:31 AM
 * Project: IntelligentSearch
 */
@Service
public class DocumentServiceImpl implements DocumentService{

    @Autowired
    private DocumentsRepository documentsRepository;

    @Override
    public List<Document> getTopDocumentsForTopic(int topic) {
        return documentsRepository.findAll(new Sort(Sort.Direction.DESC, "topicsDistribution"));
    }
}
