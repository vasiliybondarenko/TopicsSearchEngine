package vagueobjects.ir.lda.online.demo;

import vagueobjects.ir.lda.online.TopicModelAlgorithm;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/24/13
 * Time: 7:27 PM
 * Project: IntelligentSearch
 */
public interface TopicsModelAlgorithmFactory {

    TopicModelAlgorithm createTopicModel(int dictionarySize, int topics);
}
