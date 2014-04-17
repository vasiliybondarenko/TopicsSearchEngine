package intelligence.core;


import infrascructure.data.dom.Document;
import infrascructure.data.dom.DocumentImpl;
import intelligence.core.util.OnlineLDADocumentsParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/2/13
 * Time: 11:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentParserTest {

    private OnlineLDADocumentsParser parser;

    @BeforeClass
    private void setUp(){
        parser = new OnlineLDADocumentsParser();
    }

    @Test
    public void testParser() throws IOException {
        String path = "src/main/resources/docs.txt";

        ArrayList<Document> documents = parser.getDocumentsFromFile(path);

        Document doc = documents.get(0);
        Assert.assertEquals(1, doc.getIdentifier());
        Assert.assertEquals("Metallica", doc.getTitle());
        Assert.assertEquals(0.01, doc.getTopicsDistribution()[2]);
    }

    @Test
    public void testLazyParse() throws Exception {
        String path = "src/main/resources/docs.txt";

        try (Stream<DocumentImpl> documents = parser.getDocumentsFromFileLazy(path)){
            Assert.assertEquals(documents.count(), 3);
        }
    }
}
