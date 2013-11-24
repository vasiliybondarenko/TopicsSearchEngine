package infrascructure.data.parse;

import infrascructure.data.PlainTextResource;

import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 9/13/13
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlainDocValidatorImpl implements PlainDocsValidator {

    private HashSet<String> titles;

//    public PlainDocValidatorImpl(String docsPath) throws IOException {
//        IOHelper.readLinesFromFile(docsPath);
//        //this.titles = titles;
//    }

    @Override
    public boolean accept(PlainTextResource d) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
