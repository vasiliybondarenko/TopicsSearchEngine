package infrascructure.data.email;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/8/14
 * Time: 12:56 PM
 * Project: IntelligentSearch
 */
public class DefaultEmailReaderFactory extends EmailReaderFactory {

    private final String propertiesPath;

    public DefaultEmailReaderFactory(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }


    @Override
    EmailReader createEmailReader() throws Exception{
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesPath));
        return new EmailReaderImpl(properties);
    }
}
