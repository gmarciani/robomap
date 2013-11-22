package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.database.SettingDAO;
import robomap.log.Log;
import robomap.model.Location;

public class SettingJDBCDAO implements SettingDAO {
	
	private static ConnectionManager connectionManager = null;
	private static SettingJDBCDAO robotSettingDAO = null;
	
	private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS setting ("
			+ "name VARCHAR(20) NOT NULL,"
			+ "value VARCHAR(20),"
			+ "CONSTRAINT pkName PRIMARY KEY (name),"
			+ "UNIQUE INDEX idxName (name ASC))";
	
	private SettingJDBCDAO() {
		this.createTable();
	}	

	public static SettingJDBCDAO getInstance() {
		if(robotSettingDAO == null) {
			robotSettingDAO = new SettingJDBCDAO();
		}
		return robotSettingDAO;
	}
	
	private void createTable() {
		connectionManager = JDBCConnectionManager.getInstance();	
		
		Connection connection = connectionManager.getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement(SQL_CREATE_TABLE);
			statement.executeUpdate();
		} catch (SQLException exc) {
			Log.printSQLException("SettingJDBCDAO", "createTable", exc);
		} finally {
			connectionManager.close(connection, statement);
		}	
	}

	@Override
	public Location getStartLocation() {
		// TODO Auto-generated method stub
		return null;
	}

}
