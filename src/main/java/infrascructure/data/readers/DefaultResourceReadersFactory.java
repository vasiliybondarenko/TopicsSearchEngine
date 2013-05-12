package infrascructure.data.readers;


public class DefaultResourceReadersFactory extends ResourceReadersFactory{
	
	@Override
	public ResourceReader getResourceReader() {		
	    return new LocalReader();
	}
}
