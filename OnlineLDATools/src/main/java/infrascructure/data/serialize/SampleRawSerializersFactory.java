package infrascructure.data.serialize;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 10/6/13
 * Time: 8:55 PM
 * Project: IntelligentSearch
 */
public class SampleRawSerializersFactory implements RawSerializersFactory{
    @Override
    public RawResourceSerializer createSimpleSerializer(String dataDir) {
        return new SampleResourceSerializer(dataDir);
    }
}
