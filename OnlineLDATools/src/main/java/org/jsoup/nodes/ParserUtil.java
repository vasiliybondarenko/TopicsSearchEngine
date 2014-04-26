package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.jsoup.select.Elements;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/19/14
 * Time: 8:20 AM
 * Project: IntelligentSearch
 */
public class ParserUtil {
    public static Elements getElementsByTag(String tagName, Element rootElement) {
        Validate.notEmpty(tagName);
        tagName = tagName.toLowerCase().trim();

        return ElementsCollector.collect(new Evaluator.Tag(tagName), rootElement);
    }

    public static Map<Element, Integer> getElementsByTagMap(String tagName, Element rootElement) {
        Validate.notEmpty(tagName);
        tagName = tagName.toLowerCase().trim();

        return ElementsCollector.collectMap(new Evaluator.Tag(tagName), rootElement);
    }
}
