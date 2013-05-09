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
package infrascructure.data.parse;

import infrascructure.data.PlainTextResource;
import infrascructure.data.Resource;
import infrascructure.data.util.Trace;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author shredinger
 *
 */
public class DOMParser implements Parser{

    /* (non-Javadoc)
     * @see infrascructure.data.Parser#parse(infrascructure.data.Resource)
     */
    @Override
    public PlainTextResource parse(Resource r) {
	String html = r.getData();
	Document doc = Jsoup.parse(html);
	
	String query = "div.mw-content-ltr";
	Element e = doc.select(query).first();
	Elements children = e.children().select("p");
	
	String tittle = doc.title();
	String data = e.text();
	
//	for(Element el: children) {
//	    //Trace.trace(el.data());
//	    String text = el.html();
//	    String planeText = text.replaceFirst("<(.*)>", "");
//	    String txt = el.text();
//	    Trace.trace(el.html());
//	    
//	}
	
	PlainTextResource resource = new PlainTextResource(data);
	resource.setTittle(tittle);
	return resource;
    }

}
