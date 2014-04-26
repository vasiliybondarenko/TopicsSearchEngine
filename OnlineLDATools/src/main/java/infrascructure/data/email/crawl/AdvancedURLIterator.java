package infrascructure.data.email.crawl;

import infrascructure.data.email.html.entity.ResultLink;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/8/14
 * Time: 6:17 PM
 * Project: IntelligentSearch
 */
public interface AdvancedURLIterator {
    ResultLink getNextURL();
}
