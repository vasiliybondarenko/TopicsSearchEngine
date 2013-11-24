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
package infrascructure.data.util;


import java.io.*;
import java.util.*;

/**
 * @author shredinger
 */
public class IOHelper {
    public static final String FILE_SEPARATOR = File.separator;

    public static void saveToFile(String path, String data) throws IOException {
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println(data);
        }

    }

    public static void saveToFile(String path, Object data) throws IOException {
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.print(data);
        }

    }

    public static void writeLinesToFile(String path, Collection<String> lines) throws IOException {
        try (PrintWriter writer = new PrintWriter(path)) {
            Iterator<String> it = lines.iterator();
            while (it.hasNext()) {
                String line = it.next();
                writer.println(line);
            }
        }
    }

    public static void appendLineToFile(String path, String line) throws IOException {
        try (FileWriter writer = new FileWriter(path, true)) {
            writer.append(line).append("\n");
        }
    }

    public static String readFromFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder("");
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = reader.readLine();
            }
            return sb.toString();
        }
    }

    public static List<String> readLinesFromFile(String path) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line = reader.readLine();
                while (line != null) {
                    lines.add(line);
                    line = reader.readLine();
                }
            }
        }
        return lines;
    }

    public static List<String> getFiles(String sourceDirPath, FilenameFilter filter, Comparator<String> comparator) throws IOException {
        File file = new File(sourceDirPath);
        if (!file.exists()) {
            throw new IOException("Directory " + sourceDirPath + " does not exist");
        }
        if (!file.isDirectory()) {
            throw new IOException("File " + sourceDirPath + " is not a directory");
        }
        String[] files = file.list(filter);
        Arrays.sort(files, comparator);

        List<String> fullPaths = new LinkedList<>();
        for (int i = 0; i < files.length; i++) {
            String fullPath = sourceDirPath + FILE_SEPARATOR + files[i];
            fullPaths.add(fullPath);
        }
        return fullPaths;
    }
}
