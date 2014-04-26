package intelligence.core.util;

import com.google.common.base.Preconditions;
import infrascructure.data.dom.Document;
import infrascructure.data.dom.DocumentImpl;
import infrascructure.data.util.IOHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/2/13
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class OnlineLDADocumentsParser implements DocumentsParser {

    @Override
    public ArrayList<Document> getDocumentsFromFile(String path) throws IOException {
        ArrayList<Document> documents = new ArrayList<>();
        List<String> lines = IOHelper.readLinesFromFile(path);
        for(int l = 0; l < lines.size(); l ++){
            String docStr = lines.get(l);
            if(!"".equals(docStr.trim())){
                Document doc = parseDoc(docStr);
                documents.add(doc);
            }
        }
        return documents;
    }

    @Override
    public Stream<DocumentImpl> getDocumentsFromFileLazy(String path) throws IOException {
        return readDocumentsLazy(path);
    }



    public DocumentImpl parseDoc(String doc){
        Objects.requireNonNull(doc);
        String[] parts = doc.trim().split(":");
        Preconditions.checkArgument(parts.length == 3, "String '" + doc + "' cannot be parsed");
        int id = Integer.parseInt(parts[0].trim());
        String title = parts[1].trim();
        double[] topicsDistribution = getTopicsDistribution(parts[2]);
        return new DocumentImpl(id, title, topicsDistribution);
    }

    private double[] getTopicsDistribution(String line){
        String[] topicsDistributionStr = line.trim().split(" ");
        double[] topicsDistribution = new double[topicsDistributionStr.length];
        for(int t = 0; t < topicsDistribution.length; t ++){
            topicsDistribution[t] = Double.parseDouble(topicsDistributionStr[t].trim());
        }
        return topicsDistribution;
    }


    private Stream<DocumentImpl> readDocumentsLazy(String path) throws IOException {
        Path filePath = FileSystems.getDefault().getPath(path);
        BufferedReader br = Files.newBufferedReader(filePath, Charset.defaultCharset());

        //TODO: implement lazy
        return br.lines().map(s -> parseDoc(s));
    }

}
