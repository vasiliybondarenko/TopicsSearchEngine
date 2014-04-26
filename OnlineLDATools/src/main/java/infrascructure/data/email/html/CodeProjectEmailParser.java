package infrascructure.data.email.html;

import infrascructure.data.email.html.entity.ResultLink;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParserUtil;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/19/14
 * Time: 8:35 PM
 * Project: IntelligentSearch
 */
public class CodeProjectEmailParser implements EmailParser{
    private final String pattern = "www.codeproject.com/News";

    @Override
    public List<ResultLink> parse(String source) throws IOException {
        List<ResultLink> links = new ArrayList<>();
        Document doc = Jsoup.parse(source);
        Elements pElements = ParserUtil.getElementsByTag("a", doc.body());
        for (Element pElement : pElements) {
            String url = pElement.attr("href");
            String text = pElement.text();
            if(url.contains(pattern)){
                links.add(new ResultLink(url, text));
            }
        }

        return links;
    }
}
