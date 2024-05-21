package model.dao.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import es.uvigo.esei.dai.hybridserver.Configuration;
import model.dao.Html;
import model.dao.HtmlDAO;

public class HtmlController {

	private HtmlDAO htmlDAO;

	public HtmlController(Properties properties) throws SQLException {
		Connection connection = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"));
		this.htmlDAO = new HtmlDAO(connection);
	}
	
	public HtmlController(Configuration configuration) throws SQLException {
		Connection connection = DriverManager.getConnection(configuration.getDbURL(),
				configuration.getDbUser(), configuration.getDbPassword());
		this.htmlDAO = new HtmlDAO(connection);
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

	public List<Html> list() {
		return htmlDAO.list();
	}

	public String toString() {
		return htmlDAO.toString();
	}

}
