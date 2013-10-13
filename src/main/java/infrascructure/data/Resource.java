package infrascructure.data;

public class Resource implements Data{

    private String identifier;
	private String data;

	public String getData() {
		return data;
	}

    public String getIdentifier() {
        return identifier;
    }

    public Resource(String data) {
		this.data = data;
	}

    public Resource(String data, String identifier) {
        this(data);
        this.identifier = identifier;
    }
	
}
