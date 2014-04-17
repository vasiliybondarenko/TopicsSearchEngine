package infrascructure.data.readers;

import infrascructure.data.dao.ResourceMetaDataRepository;
import infrascructure.data.dom.ResourceMetaData;
import infrascructure.data.list.BigList;
import infrascructure.data.serialize.SimpleResourceSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/15/14
 * Time: 7:03 PM
 * Project: IntelligentSearch
 */
public class DBSimpleList implements BigList<ResourceMetaData>{

    private final int cacheSize;
    private int count;
    private volatile Integer lastAddedId;
    private ResourceMetaDataRepository repository;
    private SimpleResourceSerializer serializer;
    private ConcurrentHashMap<Integer, ResourceMetaData> idToMetaDataMap;

    private final ReentrantLock lock;
    private final Condition availableCond;

    public DBSimpleList(ResourceMetaDataRepository repository, SimpleResourceSerializer serializer, int cacheSize) {
        this.repository = repository;
        this.serializer = serializer;
        this.cacheSize = cacheSize;
        idToMetaDataMap = new ConcurrentHashMap<>(cacheSize);
        this.count = (int)repository.count();
        this.lastAddedId = repository.getLastId();

        this.lock = new ReentrantLock();
        this.availableCond = lock.newCondition();
    }

    @Override
    public ResourceMetaData get(int id) {
        final ReentrantLock lock = this.lock;
        lock.lock();

        if(idToMetaDataMap.containsKey(id)){
            return idToMetaDataMap.get(id);
        }
        ResourceMetaData result = readResourceFromDB(id);
        if(result != null){
            //new ResourceMetaData()
            return result;
        }

        if(id < lastAddedId){
            return null;
        }

        while (!idToMetaDataMap.containsKey(id)) {
            try {
                availableCond.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            if (idToMetaDataMap.containsKey(id)) {
                return idToMetaDataMap.get(id);
            }
            return repository.findById(id);
        } finally {
            lock.unlock();
        }

    }

    protected ResourceMetaData readResourceFromDB(int id) {
        ResourceMetaData resource = repository.findById(id);
        return resource;
        //TODO: currently all files are stored in db so serializer is used only for write
    }

    @Override
    public boolean contains(int i) {
        if(idToMetaDataMap.containsKey(i)){
           return true;
        }
        return repository.findById(i) != null;
    }

    @Override
    public void add(ResourceMetaData data) throws IOException {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            count ++;
            lastAddedId = data.getId();
            if(idToMetaDataMap.size() == cacheSize){
                ArrayList<ResourceMetaData> listToSave = new ArrayList<>(cacheSize);
                idToMetaDataMap.forEachValue(0, (v) -> listToSave.add(v));
                saveElements(listToSave);
                idToMetaDataMap.clear();
            }
            idToMetaDataMap.put(data.getId(), data);
            availableCond.signal();
        } finally {
            lock.unlock();
        }
    }

    protected void saveElements(ArrayList<ResourceMetaData> listToSave) {
        repository.save(listToSave);
        for (ResourceMetaData resourceMetaData : listToSave) {
            serializer.write(resourceMetaData, resourceMetaData.getId());
        }
    }

    @Override
    public int size() {
        return count;
    }
}
