package infrascructure.data.email.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

/**
 * Created by Roman on 08.02.14.
 */
public class DateSource {

    private File input;
    private Document doc;
    private String source;

    public DateSource(String url) throws IOException {
        this.source = url;
        this.input = new File(url);
        this.doc = Jsoup.parse(input, "UTF-8");
    }

    public String getSource() {
        return source;
    }

    public Document getDoc() {
        return doc;
    }

}
