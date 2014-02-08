package infrascructure.data.email.html;

import infrascructure.data.email.html.entity.ResultLink;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Roman on 08.02.14.
 */
public class NasaEmailParser implements EmailParser
{
    private final String source;

    public NasaEmailParser(String source)    {
        this.source = source;
    }

    public ResultLink parse() throws IOException {
        ResultLink resultLink = null;
        DataSource dateSource = new DataSource(source);
        Document doc = dateSource.getDoc();
        Elements elementRssTitle = doc.getElementsByClass("rss_title");

        for (Element elementRT : elementRssTitle) {
            resultLink = getResultLinkForRssTitle(elementRT);
        }
        return resultLink;
    }

    private ResultLink getResultLinkForRssTitle(Element elementRT) {
        Elements links  = elementRT.getElementsByTag("a");
        for (Element link : links) {
           return new ResultLink(link.attr("href"), link.text());
        }
        return null;
    }
}
