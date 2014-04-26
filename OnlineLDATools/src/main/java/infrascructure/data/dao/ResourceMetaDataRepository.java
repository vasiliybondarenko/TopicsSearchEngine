package infrascructure.data.dao;

import infrascructure.data.dom.ResourceMetaData;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/15/14
 * Time: 7:35 PM
 * Project: IntelligentSearch
 */
public interface ResourceMetaDataRepository {
    ResourceMetaData findById(Integer id);
    void  save(ResourceMetaData resourceMetaData);
    void deleteAll();
    List<ResourceMetaData> findAll();
    List<ResourceMetaData> findFirstNGreaterThanId(int id, int count);
    void save(List<ResourceMetaData> resourceMetaDataList);
    long count();
    int getLastId();
}
