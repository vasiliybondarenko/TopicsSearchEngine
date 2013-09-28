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
package infrascructure.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author shredinger
 */
public class Config {

    public final static String REQUIRED_DOCS_COUNT = "required_docs_count";
    public final static String MAX_DOCS_COUNT = "max_docs_count";
    public final static String RAWDOCS_REPOSITORY = "rawdocs_repository";
    public final static String CRAWL_THREADS = "crawl_threads";
    public final static String BATCHES_DIR = "batches_dir";
    public final static String SOCKET_READ_TIMEOUT = "readtimeout";
    public final static String VOCABULARY_PATH = "vocabulary_path";
    public final static String WORDCOUNTS_PATH = "wordCounts_path";
    public final static String PLAINDOCS_DIR = "plaindocs_dir";
    public final static String TITTLES_PATH = "tittles_path";
    public final static String MIN_DOCS_COUNT = "min_docs_count";
    public final static String QUEUE_DOCS_PATH = "queue_docs";
    public final static String STOP_WORDS_PATH = "stop_words_path";
    public final static String RAWDOCS_DIR = "rawdocs_dir";
    public final static String PARSE_DOCS_NOW = "parse_docs_now";
    //public final static String PARSE_DOCS_NOW = "parse_docs_now";




    private Properties props;

    public Config(String path) throws IOException {
        props = new Properties();
        props.load(new FileInputStream(path));
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String result = getProperty(key);
        return result == null ? defaultValue : result;
    }

    public int getPropertyInt(String key, int defaultValue){
        String resultStr = getProperty(key);
        if(resultStr == null){
            return defaultValue;
        }
        return getPropertyInt(key);
    }

    public int getPropertyInt(String key){
        String resultStr = getProperty(key);
        return Integer.parseInt(resultStr);
    }
}
