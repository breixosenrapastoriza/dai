package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class HtmlController {

	private HtmlDAO htmlDAO;

	public HtmlController(Properties properties) throws SQLException {
		
		Connection connection = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"));
		this.htmlDAO = new HtmlDBDAO(connection);
	}

	public HtmlController(Map<String, String> pages) {
		this.htmlDAO = new HtmlMapDAO(pages);
	}

	public void add(Html html) {
		htmlDAO.create(html);
	}

	public void delete(String uuid) {
		htmlDAO.delete(uuid);
	}

	public Html get(String uuid) {
		return htmlDAO.get(uuid);
	}

	public boolean contains(String uuid) {
		return htmlDAO.contains(uuid);
	}
	
	public List<Html> list(){
		return htmlDAO.list();
	}

	public String toString() {
		return htmlDAO.toString();
	}

}
