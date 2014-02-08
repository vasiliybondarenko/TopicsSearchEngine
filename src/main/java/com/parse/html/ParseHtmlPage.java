package com.parse.html;

import com.parse.html.entity.ResultLink;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Roman on 08.02.14.
 */
public class ParseHtmlPage
{
    private DateSource dateSource;

    public ParseHtmlPage(DateSource dateSource)
    {
        this.dateSource = dateSource;
    }

    public ResultLink parse()
    {
        ResultLink resultLink = null;
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
