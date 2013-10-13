package infrascructure.data.serialize;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/6/13
 * Time: 8:33 PM
 * Project: IntelligentSearch
 */
public interface PlainDocsSerializersFactory {
    PlainTextResourceSerializer createPlainTextSerializer(String dataDir, String tittlesFile);
}
