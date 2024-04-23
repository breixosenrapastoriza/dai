package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HtmlDBDAO implements HtmlDAO{

	private Connection connection;

	public HtmlDBDAO(Connection connection) throws SQLException {
		this.connection = connection;
		this.connection.createStatement().execute("CREATE TABLE IF NOT EXISTS HTML (\r\n"
				+ "    uuid CHAR(36) PRIMARY KEY,\r\n"
				+ "    content TEXT NOT NULL\r\n"
				+ ")");
	}
	
	@Override
	public void create(Html html) {
		try (Statement statement = connection.createStatement()) {
			String sql = "INSERT INTO HTML (uuid, content) VALUES (?, ?)";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, html.getUuid());
			preparedStatement.setString(2, html.getContent());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Html html) {
		try (Statement statement = connection.createStatement()) {
			String sql = "UPDATE HTML uuid = ?, content = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, html.getUuid());
			preparedStatement.setString(2, html.getContent());
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void delete(String uuid) {
		try (Statement statement = connection.createStatement()) {
			String sql = "DELETE FROM HTML WHERE uuid = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, uuid);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Html get(String uuid) {
		try (Statement statement = connection.createStatement()) {
			String sql = "SELECT * FROM HTML WHERE uuid = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, uuid);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				String content = resultSet.getString("content");
				return new Html(uuid, content);
			}
			return null;
		} catch (SQLException e) {
			return null;
		}
	}

	@Override
	public boolean contains(String uuid) {
		List<Html> list = list();
		for (Html html : list) {
			if (html.getUuid().equals(uuid)) return true;
		}
		return false;
	}

	@Override
	public List<Html> list() {
		List<Html> list = new ArrayList<Html>();
		try (Statement statement = connection.createStatement()) {
			String sql = "SELECT * FROM HTML GROUP BY uuid";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String uuid = resultSet.getString("uuid");
				String content = resultSet.getString("content");
				list.add(new Html(uuid, content));
			}
			return list;
		} catch (SQLException e) {
			return list;
		}
	}
	
	public String toString() {
		String string = "";
		List<Html> list = list();
		for (Html html : list) {
			string += html.getUuid() + ":" + html.getContent() + "\n";
		}
		return string;
	}

}
