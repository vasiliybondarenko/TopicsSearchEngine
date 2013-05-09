
package infrascructure.data.parse;

import infrascructure.data.PlainTextResource;
import infrascructure.data.Resource;
import infrascructure.data.util.Trace;

import java.io.StreamCorruptedException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shredinger
 *
 */
public class HTMLParser implements Parser{

    	/**
	 * 
	 */
	public HTMLParser() {	   
	}
    
	
	@Override
	public PlainTextResource parse(Resource r) {
	   
		String data = r.getData();
		data = data.replaceAll("<link(.*)/>", "");
		data = data.replaceAll("<meta(.*)/>", "");
		data = data.replaceAll("<style.*>(.*)</style", "");
		data = data.replaceAll("<script.*>(.*)</script>", "");
		data = data.replaceAll("<script.*/>", "");
		data = data.replaceAll("<text.*?>(.*)</text", "");
		//data = data.replaceAll("\n", " ");
		data = data.replaceAll("\\{\\{.*?\\}\\}", "");
		data = data.replaceAll("\\[\\[Category:.*", "");
		data = data.replaceAll("==\\s*[Ss]ource\\s*==.*", "");
		data = data.replaceAll("==\\s*[Rr]eferences\\s*==.*", "");
		data = data.replaceAll("==\\s*[Ee]xternal [Ll]inks\\s*==.*", "");
		data = data.replaceAll("==\\s*[Ee]xternal [Ll]inks and [Rr]eferences==\\s*", "");
		data = data.replaceAll("==\\s*[Ss]ee [Aa]lso\\s*==.*", "");
		data = data.replaceAll("http://[^\\s]*", "");
		data = data.replaceAll("\\[\\[Image:.*?\\]\\]", "");
		data = data.replaceAll("Image:.*?\\|", "");
		data = data.replaceAll("\\[\\[.*?\\|*([^\\|]*?)\\]\\]", "\\1");
		data = data.replaceAll("\\&lt;.*?&gt;", "");
		
		String reg = "(\\s)*(([a-zA-Z]+)[,|.]?(\\s)*)+";
	        Pattern pattern = Pattern.compile(reg,  Pattern.CASE_INSENSITIVE);
	        Matcher matcher = pattern.matcher(data);
	        StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
		    String s = matcher.group();
		    sb.append(s).append("\n");		    
		}
		
		return new PlainTextResource(sb.toString());
	}

}
