package infrascructure.data.email.html.entity;

import org.springframework.data.annotation.Id;

/**
 * Created by Roman on 08.02.14.
 */
public class ResultLink {

    @Id
    private String url;
    private String name;
    private String rawDocId;

    public ResultLink(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRawDocId() {
        return rawDocId;
    }

    public void setRawDocId(String rawDocId) {
        this.rawDocId = rawDocId;
    }

    @Override
    public String toString() {
        return "ResultLink{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
