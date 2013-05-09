package infrascructure.data;

public class Resource implements Data{

	private String data;
	public String getData() {
		return data;
	}
	
	public Resource(String data) {
		this.data = data;
	}
	
}
