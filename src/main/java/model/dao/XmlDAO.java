package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class XmlDAO{

	private Connection connection;

	public XmlDAO(Connection connection) throws SQLException {
		this.connection = connection;
		this.connection.createStatement().execute("CREATE TABLE IF NOT EXISTS XML (\r\n"
				+ "    uuid CHAR(36) PRIMARY KEY,\r\n" + "    content TEXT NOT NULL\r\n" + ")");
	}


	public void create(Xml xml) {
		try (Statement statement = connection.createStatement()) {
			String sql = "INSERT INTO XML (uuid, content) VALUES (?, ?)";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, xml.getUuid());
			preparedStatement.setString(2, xml.getContent());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Xml xml) {
		try (Statement statement = connection.createStatement()) {
			String sql = "UPDATE XML uuid = ?, content = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, xml.getUuid());
			preparedStatement.setString(2, xml.getContent());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void delete(String uuid) {
		try (Statement statement = connection.createStatement()) {
			String sql = "DELETE FROM XML WHERE uuid = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, uuid);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Xml get(String uuid) {
		try (Statement statement = connection.createStatement()) {
			String sql = "SELECT * FROM XML WHERE uuid = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, uuid);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				String content = resultSet.getString("content");
				return new Xml(uuid, content);
			}
			return null;
		} catch (SQLException e) {
			return null;
		}
	}

	public boolean contains(String uuid) {
		List<Xml> list = list();
		for (Xml xml : list) {
			if (xml.getUuid().equals(uuid))
				return true;
		}
		return false;
	}

	public List<Xml> list() {
		List<Xml> list = new ArrayList<>();
		try (Statement statement = connection.createStatement()) {
			String sql = "SELECT * FROM XML GROUP BY uuid";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String uuid = resultSet.getString("uuid");
				String content = resultSet.getString("content");
				list.add(new Xml(uuid, content));
			}
			return list;
		} catch (SQLException e) {
			return list;
		}
	}

	public String toString() {
		String string = "";
		List<Xml> list = list();
		for (Xml xml : list) {
			string += xml.getUuid() + ":" + xml.getContent() + "\n";
		}
		return string;
	}

}
