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
	
	private static final String SQL_CREATE_DATABASE = "CREATE SCHEMA IF NOT EXISTS robomap";

	private JDBCConnectionManager() {
		this.createDatabase();
	}
	
	public static synchronized JDBCConnectionManager getInstance() {
		if (singletonConnectionManager == null) {
			singletonConnectionManager = new JDBCConnectionManager();
		}		
		return singletonConnectionManager;
	}
	
	private void createDatabase() {	
		
		Connection connection = this.getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement(SQL_CREATE_DATABASE);
			statement.executeUpdate();
		} catch (SQLException exc) {
			Log.printSQLException("JDBCConnectionManager", "createDatabase", exc);
		} finally {
			this.close(statement);
		}
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
				Log.printSQLException("JBDCConnectionManager", "close(connection)", exc);
			}
		}
	}
	
	@Override
	public synchronized void close(PreparedStatement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException exc) {
				Log.printSQLException("JBDCConnectionManager", "close(statement)", exc);
			}
		}
	}
	
	@Override
	public synchronized void close(ResultSet result) {
		if (result != null) {
			try {
				result.close();
			} catch (SQLException exc) {
				Log.printSQLException("JBDCConnectionManager", "close(resultset)", exc);
			}
		}
	}
	
	@Override
	public synchronized void close(Connection connection, PreparedStatement statement) {
		this.close(statement);	
		this.close(connection);
	}
	
	@Override
	public synchronized void close(Connection connection, PreparedStatement statement, ResultSet result) {
		this.close(result);	
		this.close(statement);	
		this.close(connection);
	}

}
