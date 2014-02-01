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

import infrascructure.data.Data;
import infrascructure.data.util.IOHelper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shredinger
 */
public abstract class FileResourceSerializer<T extends Data> implements ResourceSerializer<T> {

    public static final String ID_TITLE_SEPARATOR = ":";

    protected String dataDirectory;

    /**
     *
     */
    public FileResourceSerializer(String dataDirectory) {
        if (dataDirectory == null || dataDirectory.equals("")) {
            throw new IllegalArgumentException("source directory cannot be empty");
        }
        File f = new File(dataDirectory);
        if (!f.isDirectory()) {
            throw new IllegalArgumentException("source directory '" + dataDirectory + "' is invalid");
        }
        this.dataDirectory = dataDirectory;
    }

    public static Map<Integer, String> parseTittles(List<String> titlesLines){
        Map<Integer, String> idToTitlesMap = new HashMap<>();
        for(String line: titlesLines){
            if(!line.isEmpty()){
                String[] parts = line.split(ID_TITLE_SEPARATOR);
                Integer id = Integer.parseInt(parts[0].trim());
                String title = parts[1].trim();
                idToTitlesMap.put(id, title);
            }
        }
        return idToTitlesMap;
    }

    protected String getPath(Integer id) {
        return dataDirectory + IOHelper.FILE_SEPARATOR + id + ".txt";
    }

}
