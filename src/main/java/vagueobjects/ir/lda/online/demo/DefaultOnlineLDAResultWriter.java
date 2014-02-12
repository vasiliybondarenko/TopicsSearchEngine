package vagueobjects.ir.lda.online.demo;

import infrascructure.data.dom.Document;
import infrascructure.data.util.CloseableWriter;

import java.util.List;
import java.util.function.Function;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/23/13
 * Time: 6:04 PM
 * Project: IntelligentSearch
 */
public class DefaultOnlineLDAResultWriter implements OnlineLDAResultWriter{

    @Override
    public void writeTopWords(OnlineLDAResult onlineLDAResult, CloseableWriter writer, int topWordsCount) {
        List<Topic> topics = onlineLDAResult.getTopics();
        System.out.println();
        for (int t = 0; t < topics.size(); t++) {
            writer.appendLine("--------- TOPIC " + t + "-------------------");
            topics.get(t).words.stream().sorted((x, y) -> Double.compare(y.probability, x.probability)).limit(topWordsCount).
                    forEachOrdered(w -> writer.appendLine(w.word + " -> " + w.probability));
            writer.appendLine("\n");
        }

    }

    @Override
    public void writeDocumentTopicsDistribution(OnlineLDAResult onlineLDAResult, CloseableWriter writer) {
        List<Document> documents = onlineLDAResult.getDocuments();
        Function<double[], String> topicsFormatter = (topics) -> {
            StringBuilder sb = new StringBuilder("");
            for (int t = 0; t < topics.length; t++) {
                sb.append(topics[t]).append(" ");
            }
            return sb.toString();
        };

        int topics = documents.get(0).getTopicsDistribution().length;
        for (int t = 0; t < topics; t++) {
            final int topic = t;
            writer.appendLine("TOPIC " + topic + ":");
            documents.stream().sorted( (x, y) -> Double.compare(y.getTopicsDistribution()[topic], x.getTopicsDistribution()[topic]) )
                    .forEachOrdered(d -> writer.appendLine(
                    new StringBuilder("").append(d.getIdentifier()).append(":").append(d.getTitle()).append(":").append(topicsFormatter.apply(d.getTopicsDistribution()))
                            .toString()
            ));
            writer.appendLine("\n");
        }
    }
}
