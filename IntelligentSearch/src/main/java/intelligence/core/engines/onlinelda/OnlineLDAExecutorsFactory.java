package intelligence.core.engines.onlinelda;

import vagueobjects.ir.lda.online.execution.BaseExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 12/29/13
 * Time: 10:34 PM
 * Project: IntelligentSearch
 */
public abstract class OnlineLDAExecutorsFactory {
    public abstract BaseExecutor createExecutor();
}
