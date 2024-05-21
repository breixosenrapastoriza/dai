package model.dao.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import es.uvigo.esei.dai.hybridserver.Configuration;
import model.dao.Html;
import model.dao.HtmlDAO;
import model.dao.Xml;
import model.dao.XmlDAO;

public class XmlController {

	private XmlDAO xmlDAO;

	public XmlController(Properties properties) throws SQLException {
		Connection connection = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"));
		this.xmlDAO = new XmlDAO(connection);
	}
	
	public XmlController(Configuration configuration) throws SQLException {
		Connection connection = DriverManager.getConnection(configuration.getDbURL(),
				configuration.getDbUser(), configuration.getDbPassword());
		this.xmlDAO = new XmlDAO(connection);
	}

	public void add(Xml xml) {
		xmlDAO.create(xml);
	}

	public void delete(String uuid) {
		xmlDAO.delete(uuid);
	}

	public Xml get(String uuid) {
		return xmlDAO.get(uuid);
	}

	public boolean contains(String uuid) {
		return xmlDAO.contains(uuid);
	}

	public List<Xml> list() {
		return xmlDAO.list();
	}

	public String toString() {
		return xmlDAO.toString();
	}


}
