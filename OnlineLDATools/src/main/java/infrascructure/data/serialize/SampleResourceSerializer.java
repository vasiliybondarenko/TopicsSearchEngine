package infrascructure.data.serialize;

import infrascructure.data.Resource;
import infrascructure.data.util.IOHelper;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 9/29/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SampleResourceSerializer extends FileResourceSerializer<Resource> implements RawResourceSerializer{

    public SampleResourceSerializer(String dataDirectory) {
        super(dataDirectory);
    }

    @Override
    public Resource read(Integer id) {
        String path = getPath(id);
        try {
            String data = IOHelper.readFromFile(path);
            String identifier = "";
            if(data.startsWith("RESOURCE_ID:")){
                identifier = data.substring(data.indexOf(":") + 1, data.indexOf("\n"));
                data = data.substring(data.indexOf("\n") + 1, data.length());
            }
            return new Resource(data, identifier);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void write(Resource data, Integer id) throws IOException {
        String path = getPath(id);
        try {
            StringBuilder dataToSave = new StringBuilder();
            dataToSave.append("RESOURCE_ID:").append(data.getIdentifier()).append("\n");
            dataToSave.append(data.getData());
            IOHelper.saveToFile(path, dataToSave.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
