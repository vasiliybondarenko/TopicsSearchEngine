package intelligence.core.util;

import infrascructure.data.dom.Document;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;
import intelligence.core.dao.DocumentsRepository;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/17/13
 * Time: 2:40 PM
 * Project: IntelligentSearch
 */
public class OnlineLDAImport {


    public static void main(String[] args) throws IOException {
        String contextPath = "///Users/shredinger/Documents/DEVELOPMENT/Projects/SHARED/IntelligentSearch/src/main/resources/test_mongodb.xml";
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(contextPath);
        DocumentsRepository documentsRepository = context.getBean(DocumentsRepository.class);

        //String dir = "/Users/shredinger/Documents/DEVELOPMENT/Projects/SHARED/OnlineLDA-Launch/data/OnlineLDA_Test_SampleData/Results";
        //List<String> files = getFiles(dir);
        //DocumentsParser parser = new OnlineLDADocumentsParser();

        //importOnlineLDAResultsToDB(parser, files, documentsRepository);


        for (int i = 0; i < 3; i++) {
            List<Document> documentsByTopic = documentsRepository.getDocumentsByTopic(i, 100);
            Trace.trace("\n\nTOPIC " + i + ":\n");
            documentsByTopic.forEach(System.out::println);
        }

    }

    private static void importOnlineLDAResultsToDB(DocumentsParser parser, List<String> files, DocumentsRepository repository) {
        repository.deleteAll();
        files.forEach((file) -> {
            try {
                ArrayList<Document> docs = parser.getDocumentsFromFile(file);
                repository.save(docs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void parseFiles(DocumentsParser parser, List<String> files) {
        files.forEach((file) -> {
            try {
                ArrayList<Document> docs = parser.getDocumentsFromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static List<String> getFiles(String dir) throws IOException {
        FilenameFilter filter = (directory, fname) -> fname.startsWith("OnlineLDA.results.docs_batch=");
        Comparator<String> comparator = (s1, s2) -> {
            String batch1 = s1.substring(s1.lastIndexOf("=") + 1, s1.lastIndexOf("."));
            String batch2 = s2.substring(s2.lastIndexOf("=") + 1, s2.lastIndexOf("."));
            return Integer.parseInt(batch1) - Integer.parseInt(batch2);
        };
        return IOHelper.getFiles(dir, filter, comparator);
    }


}
