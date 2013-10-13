package vagueobjects.ir.lda.online;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 8/4/13
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Config {

    private final static String PROPS_PATH = "onlinelda.properties";
    private Properties props;

    public static void load(Properties properties){
        instance = new Config(properties);
    }

    public static void load(String path) throws IOException {
        instance = new Config(path);
    }

    public static String getProperty(String key) {
        return instance.props.getProperty(key);
    }

    public static boolean getBooleanProperty(String key){
        return Boolean.parseBoolean(getProperty(key));
    }

    public static String getProperty(String key, String defaultValue) {
        String result = getProperty(key);
        return result == null ? defaultValue : result;
    }

    /**
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     */
    protected Config(String path) throws IOException {
        props = new Properties();
        props.load(new FileInputStream(path));
    }

    protected  Config(Properties properties){
        props = properties;
    }

    private static Config instance;


}
