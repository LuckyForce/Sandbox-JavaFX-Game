package application;

public class Control {
	String key;
	String description;
	
	public Control(String key, String description) {
		setKey(key);
		setDescription(description);
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
