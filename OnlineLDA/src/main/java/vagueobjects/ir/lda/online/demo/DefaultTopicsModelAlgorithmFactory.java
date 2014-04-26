package vagueobjects.ir.lda.online.demo;

import vagueobjects.ir.lda.online.OnlineLDA;
import vagueobjects.ir.lda.online.TopicModelAlgorithm;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/24/13
 * Time: 7:29 PM
 * Project: IntelligentSearch
 */
public class DefaultTopicsModelAlgorithmFactory implements TopicsModelAlgorithmFactory {
    @Override
    public TopicModelAlgorithm createTopicModel(int dictionarySize, int topics) {
        final double alpha = 1.d / topics;
        final double eta = 1.d / topics;

        return new OnlineLDA(dictionarySize, topics, TopicModelAlgorithm.D, alpha, eta, TopicModelAlgorithm.TAU, TopicModelAlgorithm.KAPPA);
    }
}
