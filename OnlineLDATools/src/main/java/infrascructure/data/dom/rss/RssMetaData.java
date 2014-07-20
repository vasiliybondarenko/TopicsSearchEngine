package infrascructure.data.dom.rss;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 7/18/14
 * Time: 8:56 PM
 * Project: NewTopicSearch
 */

@Document(collection = "rssLog")
public class RssMetaData {
    @Id
    private final int id;
    private final String tag;

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public RssMetaData(int id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RssMetaData that = (RssMetaData) o;

        if (id != that.id) return false;
        if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                '}';
    }
}
