package model.dao.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import es.uvigo.esei.dai.hybridserver.Configuration;
import model.dao.Html;
import model.dao.XsdDAO;
import model.dao.Xslt;
import model.dao.XsltDAO;

public class XsltController {

	private XsltDAO xsltDAO;

	public XsltController(Properties properties) throws SQLException {
		Connection connection = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"));
		this.xsltDAO = new XsltDAO(connection);
	}
	
	public XsltController(Configuration configuration) throws SQLException {
		Connection connection = DriverManager.getConnection(configuration.getDbURL(),
				configuration.getDbUser(), configuration.getDbPassword());
		this.xsltDAO = new XsltDAO(connection);
	}

	public void add(Xslt xslt) {
		xsltDAO.create(xslt);
	}

	public void delete(String uuid) {
		xsltDAO.delete(uuid);
	}

	public Xslt get(String uuid) {
		return xsltDAO.get(uuid);
	}

	public boolean contains(String uuid) {
		return xsltDAO.contains(uuid);
	}

	public List<Xslt> list() {
		return xsltDAO.list();
	}

	public String toString() {
		return xsltDAO.toString();
	}

}
