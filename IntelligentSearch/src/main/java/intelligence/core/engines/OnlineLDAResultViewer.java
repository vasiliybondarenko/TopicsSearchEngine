package intelligence.core.engines;

import infrascructure.data.Config;
import infrascructure.data.dom.Document;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;
import intelligence.core.dao.DocumentsRepository;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/9/14
 * Time: 1:15 AM
 * Project: IntelligentSearch
 */
public class OnlineLDAResultViewer {
    public static void main(String[] args) throws IOException{
        String configPath = "src/main/resources/emails/nasaEmailOnlineLDAContext_View.xml";

        String fullPath = new File(configPath).getAbsolutePath();

        Trace.trace("Configuration path: " + fullPath);

        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(configPath);
        Config config = context.getBean(Config.class);
        int topics = config.getPropertyInt(Config.TOPICS);
        String stop_words_path = config.getProperty("onlinelds.results");
        List<List<String>> topWords = getTopWords(stop_words_path);

        try(FileWriter writer = new FileWriter("results.html")){
            for (int t = 0; t < topics; t++) {
                //writer.append("TOPIC " + t + "<br>\n");

                String topWordsHtml = createTopWordsHtml(topWords.get(t), 10);
                writer.append(topWordsHtml).append("<br>");
                writer.append("<hr width=\"100%\" size=\"4\"><br>");

                DocumentsRepository documentsRepository = context.getBean(DocumentsRepository.class);
                documentsRepository.getDocumentsByTopic(t, 200).forEach((
                        d) ->
                            appendLine(writer, createLink(d) + "<br>")
                         );
                writer.append("<br><br>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void appendLine(FileWriter writer, String line){
        try {
            writer.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String createLink(Document doc){
        return String.format("<a href=\"%s\">%s</a>", doc.getUrl(), doc.getTitle());
    }

    private static List<List<String>> getTopWords(String path) throws IOException {
        List<List<String>> topics = new ArrayList<>();
        List<String> lines = IOHelper.readLinesFromFile(path);
        for(String line: lines){
            if(line.contains("TOPIC ")){
                topics.add(new ArrayList<String>());
            }else{
                int index = line.indexOf("->");
                if(index >= 0){
                    String word = line.substring(0, index - 1);
                    topics.get(topics.size() - 1).add(word);
                }

            }
        }
        return topics;
    }

    private static String createTopWordsHtml(List<String> words, int count){
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for(String w: words){
            String formattedWord = String.format("<font size=\"%s\" color=\"%s\">%s</font>", 16 - i, "magenta", getFirstUpper(w));
            sb.append(formattedWord).append("&nbsp;&nbsp;&nbsp;");
            i += 1;
            if(i == count){
                break;
            }
        }
        return sb.toString();
    }

    private static String getFirstUpper(String word){
        return word.substring(0,1).toUpperCase() + word.substring(1, word.length());
    }
}
