package org.jsoup.nodes;

import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/19/14
 * Time: 8:22 AM
 * Project: IntelligentSearch
 */
public class ElementsCollector {

    public static Elements collect (Evaluator eval, Element root) {
        Elements elements = new Elements();
        accumulateMatches(eval, elements, root);
        return elements;
    }

    public static Map<Element, Integer> collectMap (Evaluator eval, Element root) {
        LinkedHashMap<Element, Integer> elementsMap = new LinkedHashMap<>();
        accumulateMatches(eval, elementsMap, root, 0);
        return elementsMap;
    }

    private static void accumulateMatches(Evaluator eval, List<Element> elements, Element element) {
        if (eval.matches(element))
            elements.add(element);
        for (Element child: element.children())
            accumulateMatches(eval, elements, child);
    }

    private static void accumulateMatches(Evaluator eval, Map<Element, Integer> elements, Element element, int level) {
        if (eval.matches(element))
            elements.put(element, level);
        for (Element child: element.children()){
            accumulateMatches(eval, elements, child, level + 1);
        }

    }
}
