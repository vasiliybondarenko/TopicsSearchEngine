package infrascructure.data.email.html;

import infrascructure.data.email.html.entity.ResultLink;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Roman on 08.02.14.
 */
public class NasaEmailParser implements EmailParser{

    /**
     *
     * @param source html string to parse
     * @return
     * @throws IOException
     */
    public ResultLink parse(String source) throws IOException {
        ResultLink resultLink = null;
        Document doc = Jsoup.parse(source);
        Elements elementRssTitle = doc.getElementsByClass("rss_title");

        for (Element elementRT : elementRssTitle) {
            resultLink = getResultLinkForRssTitle(elementRT);
        }
        return resultLink;
    }

    private ResultLink getResultLinkForRssTitle(Element elementRT) {
        Elements links  = elementRT.getElementsByTag("a");
        for (Element link : links) {
            String url = link.attr("href");
            String redirectedUrl = getRedirectLinkFromUrl(url);
           return new ResultLink(redirectedUrl, link.text());
        }
        return null;
    }

    private String getRedirectLinkFromUrl(String url){
        int lastIndex = url.lastIndexOf("&") + 1;
        return url.substring(lastIndex, url.length());
    }
}
