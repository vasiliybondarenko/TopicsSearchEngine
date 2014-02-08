package infrascructure.data.email.html.entity;

/**
 * Created by Roman on 08.02.14.
 */
public class ResultLink {

    private String url;
    private String name;

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


    @Override
    public String toString() {
        return "ResultLink{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
