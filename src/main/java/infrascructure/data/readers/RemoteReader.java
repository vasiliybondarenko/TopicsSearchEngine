package infrascructure.data.readers;

import infrascructure.data.Config;
import infrascructure.data.Resource;
import infrascructure.data.util.Trace;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

public class RemoteReader implements ResourceReader {

    @Autowired
    private Config config;
	private int timeout;

    @PostConstruct
    private void init() {
        timeout = config.getPropertyInt(Config.SOCKET_READ_TIMEOUT, 5000);
    }

    @Override
	public Resource read(String location) {		
		try {	    
		    String body = readUrl(location);		
		    return new Resource(body);			
		} catch (Exception e) {
			Trace.trace(e);
		}		
		return null;
	}

	/**
	 * @param location
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private String readFromUrl(String location)
		throws MalformedURLException, IOException {
	    URL url = new URL(location);
	    URLConnection con = url.openConnection();
	    InputStream in = con.getInputStream();
	    String encoding = con.getContentEncoding();
	    encoding = encoding == null ? "UTF-8" : encoding;
	    String body = IOUtils.toString(in, encoding);
	    return body;
	}
	
	private String readUrl(String location) throws IOException  {
    		URL url;
        	StringBuilder body = new StringBuilder();		
        	url = new URL(location);
        	URLConnection con = url.openConnection();
        	con.setReadTimeout(timeout);
        	con.setConnectTimeout(timeout);        	
        	
        	
        	try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))){
        	    String inputLine;
        	    while ((inputLine = in.readLine()) != null) {
        		body.append(inputLine);
        	    }
        	      return body.toString();
        	}catch (SocketTimeoutException e) {        	    
		    throw new SocketTimeoutException("Read time out was expired. Time out is " + timeout + " ms");
		}
        		      
	}
	
	
}
