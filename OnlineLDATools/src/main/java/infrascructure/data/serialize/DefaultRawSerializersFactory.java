package infrascructure.data.serialize;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/6/13
 * Time: 8:37 PM
 * Project: IntelligentSearch
 */
public class DefaultRawSerializersFactory implements RawSerializersFactory {
    @Override
    public RawResourceSerializer createSimpleSerializer(String dataDir) {
        return new SimpleResourceSerializer(dataDir);
    }
}
