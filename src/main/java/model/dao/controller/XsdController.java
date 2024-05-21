package model.dao.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import es.uvigo.esei.dai.hybridserver.Configuration;
import model.dao.Html;
import model.dao.HtmlDAO;
import model.dao.Xsd;
import model.dao.XsdDAO;

public class XsdController {

	private XsdDAO xsdDAO;

	public XsdController(Properties properties) throws SQLException {
		Connection connection = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"));
		this.xsdDAO = new XsdDAO(connection);
	}

	public XsdController(Configuration configuration) throws SQLException {
		Connection connection = DriverManager.getConnection(configuration.getDbURL(),
				configuration.getDbUser(), configuration.getDbPassword());
		this.xsdDAO = new XsdDAO(connection);
	}
	
	
	public void add(Xsd xsd) {
		xsdDAO.create(xsd);
	}

	public void delete(String uuid) {
		xsdDAO.delete(uuid);
	}

	public Xsd get(String uuid) {
		return xsdDAO.get(uuid);
	}

	public boolean contains(String uuid) {
		return xsdDAO.contains(uuid);
	}

	public List<Xsd> list() {
		return xsdDAO.list();
	}

	public String toString() {
		return xsdDAO.toString();
	}

}
