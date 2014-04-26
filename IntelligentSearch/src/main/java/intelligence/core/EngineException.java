package intelligence.core;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 8/4/13
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class EngineException extends Exception {
    public EngineException(String message) {
        super(message);
    }

    public EngineException(String message, Throwable clause) {
        super(message, clause);
    }
}
