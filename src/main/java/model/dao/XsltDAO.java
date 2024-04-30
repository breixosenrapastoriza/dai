package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class XsltDAO{

	private Connection connection;

	public XsltDAO(Connection connection) throws SQLException {
		this.connection = connection;
		this.connection.createStatement().execute("CREATE TABLE IF NOT EXISTS XSLT (\r\n"
				+ "    uuid CHAR(36) PRIMARY KEY,\r\n" + "    content TEXT NOT NULL\r\n" + ")");
	}


	public void create(Xslt xslt) {
		try (Statement statement = connection.createStatement()) {
			String sql = "INSERT INTO XSLT (uuid, content, xsd) VALUES (?, ?, ?)";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, xslt.getUuid());
			preparedStatement.setString(2, xslt.getContent());
			preparedStatement.setString(3, xslt.getXsd());
			System.out.println(preparedStatement.toString());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Xslt xslt) {
		try (Statement statement = connection.createStatement()) {
			String sql = "UPDATE XSLT uuid = ?, content = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, xslt.getUuid());
			preparedStatement.setString(2, xslt.getContent());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void delete(String uuid) {
		try (Statement statement = connection.createStatement()) {
			String sql = "DELETE FROM XSLT WHERE uuid = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, uuid);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Xslt get(String uuid) {
		try (Statement statement = connection.createStatement()) {
			String sql = "SELECT * FROM XSLT WHERE uuid = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, uuid);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				String content = resultSet.getString("content");
				return new Xslt(uuid, content, "NECESITAAAAAAAAA DE ARGSSSSSSSSSS");
			}
			return null;
		} catch (SQLException e) {
			return null;
		}
	}

	public boolean contains(String uuid) {
		List<Xslt> list = list();
		for (Xslt xslt : list) {
			if (xslt.getUuid().equals(uuid))
				return true;
		}
		return false;
	}

	public List<Xslt> list() {
		List<Xslt> list = new ArrayList<>();
		try (Statement statement = connection.createStatement()) {
			String sql = "SELECT * FROM XSLT GROUP BY uuid";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String uuid = resultSet.getString("uuid");
				String content = resultSet.getString("content");
				list.add(new Xslt(uuid, content, ""));
			}
			return list;
		} catch (SQLException e) {
			return list;
		}
	}

	public String toString() {
		String string = "";
		List<Xslt> list = list();
		for (Xslt xslt : list) {
			string += xslt.getUuid() + ":" + xslt.getContent() + "\n";
		}
		return string;
	}

}
