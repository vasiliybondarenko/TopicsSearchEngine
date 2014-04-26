package infrascructure.data.parse;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 9/13/13
 * Time: 8:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocsValidator<Document> {
    boolean accept(Document d);
}
