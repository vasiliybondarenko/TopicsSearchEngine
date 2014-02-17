package infrascructure.data.util.spring;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/16/14
 * Time: 10:29 PM
 * Project: IntelligentSearch
 */
@Qualifier
@Repository
//@Autowired
@Retention(RetentionPolicy.RUNTIME)
public @interface BigListResource {
    BigListDestination resource();
}
