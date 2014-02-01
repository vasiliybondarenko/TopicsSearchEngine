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

import infrascructure.data.PlainTextResource;
import infrascructure.data.util.IOHelper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author shredinger
 */
public class PlainTextResourceSerializer extends FileResourceSerializer<PlainTextResource> {

    protected String titlesFilePath;
    protected Map<Integer, String> tittles;

    public PlainTextResourceSerializer(String dataDirectory, String titlesFile) {
        super(dataDirectory);
        this.titlesFilePath = titlesFile;
        String fullTittlesPath = dataDirectory + IOHelper.FILE_SEPARATOR + titlesFilePath;
        try {
            List<String> titlesLines = IOHelper.readLinesFromFile(fullTittlesPath);
            tittles = parseTittles(titlesLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see infrascructure.data.serialize.ResourceSerializer#read(java.lang.Integer)
     */
    @Override
    public PlainTextResource read(Integer id) {
        String path = getPath(id);
        try {
            String data = IOHelper.readFromFile(path);
            PlainTextResource resource = new PlainTextResource(data);
            String tittle = tittles.get(id);
            resource.setTittle(tittle);
            return resource;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* (non-Javadoc)
     * @see infrascructure.data.serialize.ResourceSerializer#write(infrascructure.data.Data, java.lang.Integer)
     */
    @Override
    public void write(PlainTextResource data, Integer id) throws IOException{
        if(tittles.size() != id){
            throw new IOException("Inconsistent Titles file: titles count = " + tittles.size() + " but doc id =  " + id);
        }
        try {
            String path = getPath(id);
            IOHelper.saveToFile(path, data.getData());
            tittles.put(id, data.getTittle());
            String titlesFullFilePath = getTittlesPath();
            IOHelper.appendLineToFile(titlesFullFilePath, id + ID_TITLE_SEPARATOR + data.getTittle());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected String getTittlesPath() {
        return dataDirectory + IOHelper.FILE_SEPARATOR + titlesFilePath;
    }

}
