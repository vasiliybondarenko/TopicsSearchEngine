package intelligence.core.engines;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 8/4/13
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseEngine implements Engine{
    protected Properties properties;

    protected BaseEngine(Properties properties){
        this.properties = properties;
    }
}
