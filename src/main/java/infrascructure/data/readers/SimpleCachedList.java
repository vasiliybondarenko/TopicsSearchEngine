/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package infrascructure.data.readers;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import infrascructure.data.Data;
import infrascructure.data.list.BigList;
import infrascructure.data.serialize.ResourceSerializer;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;

/**
 * @author shredinger
 *
 *cached list with buffer for write
 */
public class SimpleCachedList<T extends Data> implements BigList<T>{

    private final String DATA_FILE = "cached_list_data.txt"; 
    
    private String sourceDir;
    private int cacheSize;    
    private Map<Integer, T> cache;
    private volatile Integer index;
    
    private final ReentrantLock lock;
    private final Condition availableCond;

    protected ResourceSerializer<T> serializer;
    
    /**
     * 
     */
    public SimpleCachedList(String dataDir, int cacheSize, ResourceSerializer<T> serializer) {
	this.cacheSize = cacheSize;
	this.sourceDir = dataDir;	
	cache = new ConcurrentHashMap<Integer, T>(cacheSize);	
	index = -1;
	this.lock = new ReentrantLock();
	availableCond = lock.newCondition();
	restoreObjectData();
	this.serializer = serializer;
    }
    
 

    /* (non-Javadoc)
     * @see infrascructure.data.list.BigList#get(int)
     */
    @Override
    public T get(int i) {
	final ReentrantLock lock = this.lock;
	lock.lock();
	if(!contains(i)) {	    
	    while (!contains(i)) {
            try {
            	   availableCond.await();       		
                } catch (InterruptedException e) {			
            	   e.printStackTrace();
                }		    
	    }
	}
	try {
	    if(cache.containsKey(i)) {
		return cache.get(i);
	    }
	    T el = serializer.read(i);
	    return el;
	}finally {
	    lock.unlock();
	}
	

    }

    /* (non-Javadoc)
     * @see infrascructure.data.list.BigList#contains(int)
     */
    @Override
    public boolean contains(int i) {
	final ReentrantLock lock = this.lock;
	lock.lock();
	try {
	    return i <= index;
	}finally {
	    lock.unlock();	    
	}	
    }

    /* (non-Javadoc)
     * @see infrascructure.data.list.BigList#add(infrascructure.data.Data)
     */
    @Override
    public void add(T data) throws IOException {
	final ReentrantLock lock = this.lock;
	lock.lock();
	try {
	    cache.put(++index, data);
	    if(index > cacheSize) {
		flushCacheForWrite();		
	    }
	    availableCond.signal();
	}finally {
	   lock.unlock(); 
	}	
    }

    /* (non-Javadoc)
     * @see infrascructure.data.list.BigList#size()
     */
    @Override
    public int size() {
	final ReentrantLock lock = this.lock;
	lock.lock();
	try {
	    return index + 1;
	}finally {
	    lock.unlock();	    
	}	
    }
    
    protected void flushCacheForWrite() throws IOException {
	for(Integer i: cache.keySet()) {
	    T data = cache.get(i);
	    serializer.write(data, i);	    	    
	}
	cache.clear();
	flushObjectData();
    }
    
    protected void flushObjectData() {
	try {
	    String path = sourceDir + IOHelper.FILE_SEPARATOR + DATA_FILE;
	    IOHelper.saveToFile(path, index);
	} catch (IOException e) {	    
	    e.printStackTrace();
	}
    }
    
    /**
     * 
     */
    private void restoreObjectData() {
	try {
	    String path = sourceDir + IOHelper.FILE_SEPARATOR + DATA_FILE;
	    File f = new File(path); 	   
	    if(f.exists()) { 	        
		String data = IOHelper.readFromoFile(path).replaceAll("\\n", "");
	 	index = Integer.parseInt(data);
	     }     
	 } catch (IOException e) {	    
	 	e.printStackTrace();
	 }	
    }

}
