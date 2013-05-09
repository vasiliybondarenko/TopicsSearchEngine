package infrascructure.data.parse;

import infrascructure.data.PlainTextResource;
import infrascructure.data.Resource;

public interface Parser {
	
    PlainTextResource parse(Resource r);
}
