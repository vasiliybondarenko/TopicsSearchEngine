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
package infrascructure.data.list;

import infrascructure.data.Data;
import infrascructure.data.PlainTextResource;

/**
 * @author shredinger
 *
 */
public abstract class ResourceFactory<T extends Data> {
    public abstract T createResource(String data);
    
    public PlainTextResource createPlainTextResource(String data, String tittle) {
	PlainTextResource r = new PlainTextResource(data);
	r.setTittle(tittle);
	return r;
    }
}
