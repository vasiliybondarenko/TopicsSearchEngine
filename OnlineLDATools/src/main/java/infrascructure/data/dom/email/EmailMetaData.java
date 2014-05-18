package infrascructure.data.dom.email;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Author: shredinger
 * Date: 5/18/14
 * Time: 3:14 PM
 * Project: NewTopicSearch
 */

@Document(collection = "emailsLog")
public class EmailMetaData {

    @Id
    private final int id;
    private final String description;
    private final Date date;
    private final String tag;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getTag() {
        return tag;
    }

    public EmailMetaData(int id, String description, Date date, String tag) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailMetaData that = (EmailMetaData) o;

        if (id != that.id) return false;
        if (!date.equals(that.date)) return false;
        if (!description.equals(that.description)) return false;
        if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + description.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }
}
