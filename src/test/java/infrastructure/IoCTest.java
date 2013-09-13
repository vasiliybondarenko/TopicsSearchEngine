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
package infrastructure;

import infrascructure.data.Config;
import infrascructure.data.crawl.RandomWikiCrawler;
import infrascructure.data.crawl.URLIterator;
import infrascructure.data.parse.Parser;
import infrascructure.data.parse.PlainDocsRepository;
import infrascructure.data.readers.ResourceReader;
import infrascructure.data.readers.ResourcesRepository;
import infrascructure.data.stripping.Stemmer;
import infrascructure.data.vocabulary.BaseVocabularyBuilder;
import infrascructure.data.vocabulary.VocabularyBuilder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;

import static org.junit.Assert.assertNotNull;


/**
 * @author shredinger
 *
 */
public class IoCTest {

    @Test
    public void testIoC() {
	//AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);

	String testConfigPath = "src/main/resources/custombeans.xml";
	ApplicationContext context = new FileSystemXmlApplicationContext(testConfigPath);
	//appContext = new AnnotationConfigApplicationContext("infrascructure.data");

    Config config = context.getBean(Config.class);

	Parser parser = context.getBean(Parser.class);	
	URLIterator iterator = context.getBean(URLIterator.class);
	ResourceReader reader = context.getBean(ResourceReader.class);	
	
	ResourcesRepository repo = context.getBean(ResourcesRepository.class);
	PlainDocsRepository docsRepo = context.getBean(PlainDocsRepository.class);

    VocabularyBuilder vb = context.getBean(BaseVocabularyBuilder.class);
    Stemmer stemmer = context.getBean(Stemmer.class);

	assertNotNull(iterator);
	assertNotNull(reader);
	assertNotNull(parser);
	assertNotNull(repo);
	assertNotNull(docsRepo);
    assertNotNull(vb);
    assertNotNull(stemmer);

	//AnnotationConfigApplicationContext acc = new AnnotationConfigApplicationContext("my.test.spring.core");
    }

    @Test
    public void testCustomBeans(){
        String customBeansPath = "src/main/resources/custombeans.xml";
        //String customBeansPath = "custombeans.xml";
        File f = new File(customBeansPath);
        System.out.println(f.getPath());


        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(customBeansPath);
        RandomWikiCrawler crawler = context.getBean(RandomWikiCrawler.class);
        assertNotNull(crawler);




    }
}
