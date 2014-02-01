package vagueobjects.ir.lda.online.analysis;

import infrascructure.data.util.IOHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/31/14
 * Time: 9:30 PM
 * Project: IntelligentSearch
 */
public class Counter{

    private Map<String, Integer> wordCounts;
    private int totalDocs;

    public Counter(int totalDocs, String totalWordCountsPath) throws IOException {
        this.totalDocs = totalDocs;
        wordCounts = new HashMap<>();
        List<String> lines = IOHelper.readLinesFromFile(totalWordCountsPath);
        for(String line: lines){
            if(!line.isEmpty()){
                String[] parts = line.split("\\s");
                String word = parts[0].trim();
                Integer count = Integer.parseInt(parts[1].trim());
                wordCounts.put(word, count);
            }
        }
    }

    public double getTF_IDF(String word, int totalWordsInDoc, int countInDoc){
        int docsCount = wordCounts.get(word);
        double tf = (double)countInDoc / (double)totalWordsInDoc;
        double idf = Math.log(totalDocs / docsCount);

        return tf * idf;
    }
}
