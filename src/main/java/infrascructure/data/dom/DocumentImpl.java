package infrascructure.data.dom;

import org.springframework.data.annotation.Id;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/2/13
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentImpl implements Document{

    public DocumentImpl(int identifier, String title, double[] topicsDistribution) {
        this.identifier = identifier;
        this.title = title;
        this.topicsDistribution = topicsDistribution;
    }

    @Id
    private int identifier;

    private String title;
    private double[] topicsDistribution;

    @Override
    public int getIdentifier() {
        return identifier;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public double[] getTopicsDistribution() {
        return topicsDistribution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentImpl document = (DocumentImpl) o;

        if (identifier != document.identifier) return false;
        if (!title.equals(document.title)) return false;
        if (!Arrays.equals(topicsDistribution, document.topicsDistribution)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = identifier;
        result = 31 * result + title.hashCode();
        result = 31 * result + Arrays.hashCode(topicsDistribution);
        return result;
    }

    @Override
    public String toString() {
        return "DocumentImpl{" +
                "identifier=" + identifier +
                ", title='" + title + '\'' +
                ", topicsDistribution=" + Arrays.toString(topicsDistribution) +
                '}';
    }
}
