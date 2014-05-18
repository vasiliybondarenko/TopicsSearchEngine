package infrascructure.data.dao;

import infrascructure.data.dom.email.EmailMetaData;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 5/18/14
 * Time: 3:51 PM
 * Project: NewTopicSearch
 */


public interface CustomEmailMetaDataRepository {
    List<EmailMetaData> getLastItems(int limit);
}
