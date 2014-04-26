package infrascructure.data.parse;

import infrascructure.data.PlainTextResource;
import infrascructure.data.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParserUtil;
import org.jsoup.select.Elements;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/19/14
 * Time: 9:53 PM
 * Project: IntelligentSearch
 */
public class PParser implements Parser{

    @Override
    public PlainTextResource parse(Resource r){
        Document doc = Jsoup.parse(r.getData());

        Element rootElement = doc.body();

        Elements pElements = ParserUtil.getElementsByTag("p", rootElement);
        StringBuilder sb = new StringBuilder("");
        for (Element el : pElements) {
            String text = el.text();
            sb.append(text).append("\n");
        }

        String tittle = doc.title();
        PlainTextResource resource = new PlainTextResource(sb.toString());
        resource.setTittle(tittle);
        return resource;
    }
}
