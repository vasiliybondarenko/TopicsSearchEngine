package infrascructure.data.dom;

import com.google.common.base.Preconditions;
import infrascructure.data.Resource;
import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/15/14
 * Time: 7:05 PM
 * Project: IntelligentSearch
 */
public class ResourceMetaData extends Resource{
    public final static int MAX_TAGS_COUNT = 10;

    @Id
    private final Integer id;
    private final String data;
    private final Tag[] tags;
    private final Map<Tags, Tag> tagsMap;

    public Integer getId() {
        return id;
    }

    public Tag getTag(Tags tag){
        return tagsMap.get(tag);
    }

    public ResourceMetaData(Integer id, String data, Tag... tags) {
        super(data, String.valueOf(id));
        Preconditions.checkArgument(id != null, "id cannot be null");
        Preconditions.checkArgument(data != null, "data cannot be null");
        Preconditions.checkArgument(tags != null, "tags cannot be null");
        this.id = id;
        this.data = data;
        this.tags = tags;
        this.tagsMap = new HashMap<>();
        for (int i = 0; i < tags.length; i++) {
            this.tagsMap.put(tags[i].getType(), tags[i]);
        }
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceMetaData that = (ResourceMetaData) o;

        if (!data.equals(that.data)) return false;
        if (!id.equals(that.id)) return false;
        if (!Arrays.equals(tags, that.tags)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + data.hashCode();
        result = 31 * result + Arrays.hashCode(tags);
        return result;
    }

    @Override
    public String toString() {
        return "ResourceMetaData{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}
