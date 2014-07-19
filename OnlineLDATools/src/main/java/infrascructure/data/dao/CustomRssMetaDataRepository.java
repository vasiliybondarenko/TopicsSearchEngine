package infrascructure.data.dao;

import infrascructure.data.dom.rss.RssMetaData;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 7/18/14
 * Time: 9:02 PM
 * Project: NewTopicSearch
 */
public interface CustomRssMetaDataRepository {
    List<RssMetaData> getLastItems(int limit, String tag);
}
