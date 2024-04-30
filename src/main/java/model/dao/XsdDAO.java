package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class XsdDAO{

	private Connection connection;

	public XsdDAO(Connection connection) throws SQLException {
		this.connection = connection;
		this.connection.createStatement().execute("CREATE TABLE IF NOT EXISTS XSD (\r\n"
				+ "    uuid CHAR(36) PRIMARY KEY,\r\n" + "    content TEXT NOT NULL\r\n" + ")");
	}


	public void create(Xsd xsd) {
		try (Statement statement = connection.createStatement()) {
			String sql = "INSERT INTO XSD (uuid, content) VALUES (?, ?)";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, xsd.getUuid());
			preparedStatement.setString(2, xsd.getContent());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Xsd xsd) {
		try (Statement statement = connection.createStatement()) {
			String sql = "UPDATE XSD uuid = ?, content = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, xsd.getUuid());
			preparedStatement.setString(2, xsd.getContent());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void delete(String uuid) {
		try (Statement statement = connection.createStatement()) {
			String sql = "DELETE FROM XSD WHERE uuid = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, uuid);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Xsd get(String uuid) {
		try (Statement statement = connection.createStatement()) {
			String sql = "SELECT * FROM XSD WHERE uuid = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, uuid);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				String content = resultSet.getString("content");
				return new Xsd(uuid, content);
			}
			return null;
		} catch (SQLException e) {
			return null;
		}
	}

	public boolean contains(String uuid) {
		List<Xsd> list = list();
		for (Xsd xsd : list) {
			if (xsd.getUuid().equals(uuid))
				return true;
		}
		return false;
	}

	public List<Xsd> list() {
		List<Xsd> list = new ArrayList<>();
		try (Statement statement = connection.createStatement()) {
			String sql = "SELECT * FROM XSD GROUP BY uuid";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String uuid = resultSet.getString("uuid");
				String content = resultSet.getString("content");
				list.add(new Xsd(uuid, content));
			}
			return list;
		} catch (SQLException e) {
			return list;
		}
	}

	public String toString() {
		String string = "";
		List<Xsd> list = list();
		for (Xsd xsd : list) {
			string += xsd.getUuid() + ":" + xsd.getContent() + "\n";
		}
		return string;
	}

}
