package model.dao;

public class Xslt{
	private String uuid;
	private String content;
	private String xsd;
	
	public Xslt(String uuid, String content, String xsd) {
		this.uuid = uuid;
		this.content = content;
		this.xsd = xsd;
	}
	
	public String getUuid() {
		return this.uuid;
	}

	public String getContent() {
		return this.content;
	}

	public String getXsd() {
		return this.xsd;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setXsd(String xsd) {
		this.xsd = xsd;
	}
}
