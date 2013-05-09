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

import infrascructure.data.util.Trace;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author shredinger
 *
 */
public class Config {
    
    private final static String PROPS_PATH = "crawler.properties";
    private Properties props;
    
    /**
     * @throws IOException 
     * @throws FileNotFoundException 
     * 
     */
    protected Config() throws IOException {
	props = new Properties();
	props.load(new FileInputStream(PROPS_PATH));
    }
    
    private static Config instance;
    
    public static String getProperty(String key) {
	try {
    	   if(instance == null) {
    	       instance = new Config();
    	   }
	}catch(IOException ex) {
	    Trace.trace(ex);
	}
	return instance.props.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
	String result = getProperty(key);
	return result == null ? defaultValue : result;
    }
}
