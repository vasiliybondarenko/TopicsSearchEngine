package infrascructure.data.email.html;

import infrascructure.data.email.html.entity.ResultLink;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 2/8/14
 * Time: 5:52 PM
 * Project: IntelligentSearch
 */
public interface EmailParser {
    public List<ResultLink> parse(String source) throws IOException;
}
