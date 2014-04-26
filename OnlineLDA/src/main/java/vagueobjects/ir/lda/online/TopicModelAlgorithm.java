package vagueobjects.ir.lda.online;

import vagueobjects.ir.lda.tokens.OnlineLDASource;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/23/13
 * Time: 5:29 PM
 * Project: IntelligentSearch
 */
public interface TopicModelAlgorithm {
    double MEAN_CHANGE_THRESHOLD = 1e-5;
    int NUM_ITERATIONS = 200;

    static final int D = 10800;
    static final double TAU = 1d;
    static final double KAPPA = 0.8d;

    Result workOn(OnlineLDASource docs);
}
