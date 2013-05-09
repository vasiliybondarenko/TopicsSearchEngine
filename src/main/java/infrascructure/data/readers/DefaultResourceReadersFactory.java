package infrascructure.data.readers;

public class DefaultResourceReadersFactory extends ResourceReadersFactory{
	
	@Override
	public ResourceReader getResourceReader() {
		//return new RemoteReader();
	    return new LocalReader();
	}
}
