package infrascructure.data.serialize;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/6/13
 * Time: 8:39 PM
 * Project: IntelligentSearch
 */
public class DefaultPlainDocsSerializersFactory implements PlainDocsSerializersFactory{
    @Override
    public PlainTextResourceSerializer createPlainTextSerializer(String dataDir, String tittlesFile) {
        return new PlainTextResourceSerializer(dataDir, tittlesFile);
    }
}
