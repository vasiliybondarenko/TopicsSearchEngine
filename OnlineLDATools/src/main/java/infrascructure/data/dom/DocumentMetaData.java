package infrascructure.data.dom;

import org.springframework.data.annotation.Id;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/11/14
 * Time: 4:18 PM
 * Project: IntelligentSearch
 */
public class DocumentMetaData {

    @Id
    private final int identifier;
    private final String title;
    private final String filePath;
    private final boolean isProcessed;

    public DocumentMetaData(int identifier, String title, String filePath, boolean isProcessed) {
        this.identifier = identifier;
        this.title = title;
        this.filePath = filePath;
        this.isProcessed = isProcessed;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getTitle() {
        return title;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentMetaData that = (DocumentMetaData) o;

        if (identifier != that.identifier) return false;
        if (isProcessed != that.isProcessed) return false;
        if (!filePath.equals(that.filePath)) return false;
        if (!title.equals(that.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = identifier;
        result = 31 * result + title.hashCode();
        result = 31 * result + filePath.hashCode();
        result = 31 * result + (isProcessed ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DocumentMetaData{" +
                "identifier=" + identifier +
                ", title='" + title + '\'' +
                ", filePath='" + filePath + '\'' +
                ", isProcessed=" + isProcessed +
                '}';
    }
}
