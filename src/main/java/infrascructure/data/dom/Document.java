package infrascructure.data.dom;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/2/13
 * Time: 8:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Document {
    int getIdentifier();
    String getTitle();
    String getUrl();
    void setUrl(String url);
    double[] getTopicsDistribution();
}
