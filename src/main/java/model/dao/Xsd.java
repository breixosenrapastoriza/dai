package model.dao;

public class Xsd{

	private String uuid;
	private String content;
	
	public Xsd(String uuid, String content) {
		this.uuid = uuid;
		this.content = content;
	}
	
	public String getUuid() {
		return this.uuid;
	}

	public String getContent() {
		return this.content;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
