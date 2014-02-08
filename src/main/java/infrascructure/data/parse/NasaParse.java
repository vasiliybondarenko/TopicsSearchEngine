package data.parse;

import data.PlainTextResource;
import data.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Roman on 08.02.14.
 */
public class NasaParse implements Parser {

    public static final String TEG_DIV_PANE_CONTENT = "div.pane-content";
    public static final String TEG_DIV_PANE_TITLE = "h2.pane-title";

    @Override
    public PlainTextResource parse(Resource r) {

        Document doc = Jsoup.parse(r.getData());

        //TODO:  element div.pane-content is not generic
        Elements elementContent = doc.select(TEG_DIV_PANE_CONTENT) ;
        Elements elementTitle = doc.select(TEG_DIV_PANE_TITLE) ;

        validationEmptyElement(elementContent, elementTitle);

        return setPlainTextResource(elementContent, elementTitle);
    }

    private PlainTextResource setPlainTextResource(Elements elementContent, Elements elementTitle) {
        PlainTextResource plainTextResource = null;
        for(Element element : elementContent)
        {
            plainTextResource= new PlainTextResource(element.text());
        }
        plainTextResource.setTittle( elementTitle.iterator().next().text());
        return plainTextResource;
    }

    private void validationEmptyElement(Elements elementContent, Elements elementTitle) {
        if(elementContent.isEmpty())
            throw new RuntimeException("No such element div.pane-content");
        if(elementTitle.isEmpty())
            throw new RuntimeException("No such element h2.pane-title");
    }
}
