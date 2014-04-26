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
package infrascructure.data.launch;

import infrascructure.data.util.IOHelper;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author shredinger
 */
public class DefaultDirectoryReader implements DirectoryReader {

    private String sourceDirPath;
    private boolean shuffleInputFiles;

    /**
     *
     */
    public DefaultDirectoryReader(String sourceDirPath) {
        this.sourceDirPath = sourceDirPath;
    }

    public DefaultDirectoryReader(String sourceDirPath, boolean shuffleInputFiles) {
        this(sourceDirPath);
        this.shuffleInputFiles = shuffleInputFiles;
    }

    public List<String> getFiles() throws IOException {

        final String pattern = "[\\d+]+\\.txt";
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.matches(pattern);
            }
        };

        Comparator<String> filesComparator = new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1.substring(0, o1.lastIndexOf("."))) - Integer.parseInt(o2.substring(0, o2.lastIndexOf(".")));

            }
        };

        List<String> files = IOHelper.getFiles(sourceDirPath, filter, null);
        if(shuffleInputFiles){
            Collections.shuffle(files);
        }

        return files;
    }
}
