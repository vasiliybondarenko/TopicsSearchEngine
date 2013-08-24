package infrastructure;

import infrascructure.data.Resource;
import infrascructure.data.parse.DOMParser;
import infrascructure.data.parse.Parser;
import infrascructure.data.readers.RemoteReader;
import infrascructure.data.readers.ResourceReader;
import infrascructure.data.util.Trace;
import org.junit.Before;
import org.junit.Test;

public class ResourceReadersTest {

	private ResourceReader reader;
	
	@Before
	public void prepare() {		
		reader = new RemoteReader();
	}
	
	@Test
	public void testRemoteReader() {
		
//		String url = "http://en.wikipedia.org/wiki/Latent_Dirichlet_allocation";
//		Resource resource = reader.read(url);
		
		//Trace.trace(resource.getData());
	}
	
	@Test
	public void testHTMLTarser() {
		
//		String url = "http://en.wikipedia.org/wiki/Latent_Dirichlet_allocation";
//		Resource resource = reader.read(url);
//		
//		String unparsedData = resource.getData();
//		
//		Parser parser = new HTMLParser();
//		String parsedData = parser.parse(resource);
//		Trace.trace(parsedData);
//		
//		String input = "<script src=\"//bits.wikimedia.org/en.wikipedia.org/load.php?debug=false&amp;lang=en&amp;modules=skins.vector&amp;only=scripts&amp;skin=vector&amp;*\"></script>";
//		String actual = parser.parse(new Resource(input));
//		Assert.assertEquals("", actual);
	}
	
	@Test
	public void testDOMTarser() {
		
		String url = "http://en.wikipedia.org/wiki/Alfred_Potier";
		Resource resource = reader.read(url);
		
		String unparsedData = resource.getData();
		
		Parser parser = new DOMParser();
		String parsedData = parser.parse(resource).getData();
		Trace.trace(parsedData);
		
		
	}
	
	@Test
	public void testCacheableReader() {
		
//		CacheableReader reader = new CacheableReader();
//		try {
//		    reader.readAll();
//		} catch (IOException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
		
		
	}
	
//	@Test
//	public void testCachedList() {
//		String sourceDir = Config.getProperty("rawdocs_dir");				
//		SimpleResourceSerializer serializer = SerializersFactory.createSimpleSerializer(sourceDir);		
//		BigList<Resource> rawdocs = new SimpleCachedList<Resource>(sourceDir, 10, serializer);		
//		try {
//		    rawdocs.add(new Resource("Metallica"));
//		    Assert.assertEquals(rawdocs.contains(0), true);
//		    Assert.assertEquals(rawdocs.size(), 1);
//		    String s = rawdocs.get(0).getData();
//		    Assert.assertEquals(rawdocs.get(0).getData(), "Metallica");
//		    
//		    
//		} catch (IOException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
//		
//	}
//	
//	@Test
//	public void testCachedReader() {
//	    ResourcesRepository reader = new ResourcesRepository();
//	    try {
//		reader.readAll();
//		Resource r;
//		int i = 0;
//		List<Resource> docs = new LinkedList<Resource>();
//		while((r = reader.get(i ++)) != null) {
//		    docs.add(r);
//		}		
//		Trace.trace("Docs downloaded: " + docs.size());
//		
//		//Assert.assertEquals(docs.size(), 11);
//		
//		PlainDocsRepository docsRepo = new PlainDocsRepository(reader);
//		docsRepo.readAll();
//		PlainTextResource doc = docsRepo.get(9);
//		
//		Trace.trace("TITTLE: " + doc.getTittle());		
//		
//	    } catch (IOException e) {
//		e.printStackTrace();
//	    }
//	}

}







