
package infrascructure.data.readers;

import infrascructure.data.Resource;


/**
 * @author shredinger
 *
 */
public interface ResourceReader {
	Resource read(String location);
}
