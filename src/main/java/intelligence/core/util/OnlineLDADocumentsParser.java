package intelligence.core.util;

import com.google.common.base.Preconditions;
import infrascructure.data.util.IOHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/2/13
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class OnlineLDADocumentsParser implements DocumentsParser {

    @Override
    public ArrayList<Document> readDocuments(String path) throws IOException {
        ArrayList<Document> documents = new ArrayList<>();
        List<String> lines = IOHelper.readLinesFromFile(path);
        for(int l = 0; l < lines.size(); l ++){
            Document doc = parseDoc(lines.get(l));
            documents.add(doc);
        }

        return documents;
    }

    public DocumentImpl parseDoc(String doc){
        Objects.requireNonNull(doc);
        String[] parts = doc.trim().split(":");
        Preconditions.checkArgument(parts.length == 3, "String " + doc + " cannot be parsed");
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
}
