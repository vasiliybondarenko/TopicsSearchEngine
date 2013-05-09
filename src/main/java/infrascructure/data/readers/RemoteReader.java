package infrascructure.data.readers;

import infrascructure.data.Resource;
import infrascructure.data.util.Trace;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

public class RemoteReader implements ResourceReader {

	@Override
	public Resource read(String location) {
		
		try {
			URL url = new URL(location);
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			String body = IOUtils.toString(in, encoding);
			return new Resource(body);			
		} catch (Exception e) {
			Trace.trace(e);
		}
		
		return null;
	}
	
	
}
