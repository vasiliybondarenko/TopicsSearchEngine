package infrascructure.data.serialize;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/6/13
 * Time: 8:32 PM
 * Project: IntelligentSearch
 */
public interface RawSerializersFactory {
    RawResourceSerializer createSimpleSerializer(String dataDir);
}
