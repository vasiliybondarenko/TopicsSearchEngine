package infrascructure.data.dao;

import com.google.common.base.Preconditions;
import infrascructure.data.dom.ResourceMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/15/14
 * Time: 7:37 PM
 * Project: IntelligentSearch
 */

public class ResourceMetaDataRepositoryImpl implements ResourceMetaDataRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private final String collectionName;


    public ResourceMetaDataRepositoryImpl(String collectionName) {
        Preconditions.checkArgument(collectionName != null, "collection name cannot be null");
        this.collectionName = collectionName;
    }

    @Override
    public ResourceMetaData findById(Integer id) {
        return mongoTemplate.findById(id, ResourceMetaData.class, collectionName);
    }

    @Override
    public void save(ResourceMetaData resourceMetaData) {
        mongoTemplate.save(resourceMetaData, collectionName);
    }

    @Override
    public void deleteAll() {
        mongoTemplate.remove(query(new Criteria()), collectionName);
    }

    @Override
    public List<ResourceMetaData> findAll() {
        return mongoTemplate.findAll(ResourceMetaData.class, collectionName);
    }

    @Override
    public List<ResourceMetaData> findFirstNGreaterThanId(int id, int count) {
        return mongoTemplate.find(query(new Criteria("id").gte(id)).limit(count), ResourceMetaData.class, collectionName);
    }

    @Override
    public void save(List<ResourceMetaData> resourceMetaDataList) {
        mongoTemplate.insert(resourceMetaDataList, collectionName);
    }

    @Override
    public long count() {
        return mongoTemplate.count(query(new Criteria()), collectionName);
    }

    @Override
    public int getLastId() {
        ResourceMetaData lastAddedDoc = mongoTemplate.findOne(query(new Criteria()).with(new Sort(Sort.Direction.DESC, "_id")).limit(1), ResourceMetaData.class, collectionName);
        return lastAddedDoc == null ? -1 : lastAddedDoc.getId();
    }


}
