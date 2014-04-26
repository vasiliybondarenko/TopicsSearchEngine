package infrascructure.data.dom;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/15/14
 * Time: 8:11 PM
 * Project: IntelligentSearch
 */
public enum Tags {
    TITLE(0), LOCAL_PATH(1), URL(2), EMAIL_SUBJECT(3), EMAIL_DATE(4);

    private final int tag;

    Tags(int tag) {
        this.tag = tag;
    }
}
