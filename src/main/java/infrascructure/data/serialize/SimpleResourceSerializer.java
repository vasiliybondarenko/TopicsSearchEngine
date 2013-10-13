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
package infrascructure.data.serialize;

import infrascructure.data.Resource;
import infrascructure.data.util.IOHelper;

import java.io.IOException;

/**
 * @author shredinger
 */
public class SimpleResourceSerializer extends FileResourceSerializer<Resource> implements RawResourceSerializer{

    /**
     * @param dataDirectory
     */
    public SimpleResourceSerializer(String dataDirectory) {
        super(dataDirectory);
    }

    /* (non-Javadoc)
     * @see infrascructure.data.list.ResourceSerializer#read(java.lang.Integer)
     */
    @Override
    public Resource read(Integer id) {
        String path = getPath(id);
        try {
            String data = IOHelper.readFromFile(path);
            return new Resource(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* (non-Javadoc)
     * @see infrascructure.data.list.ResourceSerializer#write(infrascructure.data.Data, java.lang.Integer)
     */
    @Override
    public void write(Resource data, Integer id) {
        String path = getPath(id);
        try {
            IOHelper.saveToFile(path, data.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
