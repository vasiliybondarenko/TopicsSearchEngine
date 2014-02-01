package vagueobjects.ir.lda.online.analysis;

import vagueobjects.ir.lda.tokens.Vocabulary;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/31/14
 * Time: 9:36 PM
 * Project: IntelligentSearch
 */
public class Analyser {

    public static Iterable<WordMetaData> getWords(Vocabulary vocabulary, Map<Integer, Integer> wordCounts, List<String> tokens){
        try {
            Counter counter = new Counter(3000, "/Users/shredinger/Documents/DEVELOPMENT/Projects/SHARED/OnlineLDA-Launch/data/OnlineLDA_Test_SampleData/WordCounts.txt");
            return getSortedWords(vocabulary, counter, wordCounts, tokens);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public static void printWords(Iterable<WordMetaData> words, String tf_idf_path, int min_tokens_count){
        try(FileWriter writer = new FileWriter(tf_idf_path, true)) {
            int count = 0;
            for(WordMetaData wordMetaData: words){
                writer.append(wordMetaData.toString() + "\n");
                ++ count;
            }
            writer.append("-----------------------------------[tokens: " + (count < min_tokens_count ? "<" + min_tokens_count : count) + "]\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<WordMetaData> getSortedWords(Vocabulary vocab, Counter counter, Map<Integer, Integer> wordCounts, List<String> tokens) {
        List<WordMetaData> sortedWords = new ArrayList<>();
        int totalWordsInDoc = tokens.size();
        for(Integer id: wordCounts.keySet()){
            String word = vocab.getToken(id);
            int count = wordCounts.get(id);
            double tf_idf = counter.getTF_IDF(word, totalWordsInDoc, count);
            sortedWords.add(new WordMetaData(word, tf_idf));
        }
        Collections.sort(sortedWords);
        return sortedWords;
    }
}
