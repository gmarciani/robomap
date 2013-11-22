package robomap.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.log.Log;

public class JDBCConnectionManager implements ConnectionManager {
	
private static JDBCConnectionManager singletonConnectionManager = null;
	
	private static final String URL = "jdbc:mysql://localhost:3306/robomap";
	private static final String USER = "root";
	private static final String PASSWORD = "password";

	protected JDBCConnectionManager() {}
	
	public static synchronized JDBCConnectionManager getInstance() {
		if (singletonConnectionManager == null) {
			singletonConnectionManager = new JDBCConnectionManager();
		}		
		return singletonConnectionManager;
	}
	
	@Override
	public synchronized Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException exc) {
			Log.printSQLException("JBDCConnectionManager", "getConnection", exc);
			return null;
		}
	}	
	
	@Override
	public synchronized void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException exc) {
				Log.printSQLException("JBDCConnectionManager", "close", exc);
			}
		}
	}
	
	@Override
	public synchronized void close(Connection connection, PreparedStatement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException exc) {
				Log.printSQLException("JBDCConnectionManager", "close", exc);
			}
		}		
		close(connection);
	}
	
	@Override
	public synchronized void close(Connection connection, PreparedStatement statement, ResultSet result) {
		if (result != null) {
			try {
				result.close();
			} catch (SQLException exc) {
				Log.printSQLException("JBDCConnectionManager", "close", exc);
			}
		}		
		close(connection, statement);
	}

}
