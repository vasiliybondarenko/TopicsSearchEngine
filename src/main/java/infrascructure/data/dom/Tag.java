package infrascructure.data.dom;

import org.springframework.data.annotation.Id;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/15/14
 * Time: 8:14 PM
 * Project: IntelligentSearch
 */
public class Tag {
    @Id
    private final Tags type;
    private final String value;

    public Tags getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Tag(Tags type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (type != tag.type) return false;
        if (value != null ? !value.equals(tag.value) : tag.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
