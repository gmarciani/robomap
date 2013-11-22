package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.database.HomeDAO;
import robomap.log.Log;
import robomap.model.Home;

public class HomeJDBCDAO implements HomeDAO {
	
	private static ConnectionManager connectionManager = null;
	private static HomeJDBCDAO homeDAO = null;
	
	private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS home ("
			+ "id INT UNSIGNED NOT NULL AUTO_INCREMENT,"
			+ "name VARCHAR(20) NOT NULL,"
			+ "width INT UNSIGNED NOT NULL,"
			+ "height INT UNSIGNED NOT NULL,"
			+ "CONSTRAINT pkHome PRIMARY KEY (id))";
	
	private HomeJDBCDAO() {
		this.createTable();
	}	

	public static HomeJDBCDAO getInstance() {
		if(homeDAO == null) {
			homeDAO = new HomeJDBCDAO();
		}
		return homeDAO;
	}
	
	private void createTable() {
		connectionManager = JDBCConnectionManager.getInstance();	
		
		Connection connection = connectionManager.getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement(SQL_CREATE_TABLE);
			statement.executeUpdate();
		} catch (SQLException exc) {
			Log.printSQLException("HomeJDBCDAO", "createTable", exc);
		} finally {
			connectionManager.close(connection, statement);
		}		
	}

	@Override
	public Home findHome(String homeName) {
		// TODO Auto-generated method stub
		return null;
	}

}
