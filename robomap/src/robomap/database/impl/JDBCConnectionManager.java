package robomap.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.database.DatabaseDebug;
import robomap.log.Log;

/**
 * @project robomap
 *
 * @package robomap.database.impl
 *
 * @class JDBCConnectionManager
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class JDBCConnectionManager implements ConnectionManager {
	
	private static JDBCConnectionManager connectionManager;
	
	private static final String URL = "jdbc:mysql://localhost:3306/robomap";
	private static final String USER = "root";
	private static final String PASSWORD = "password";

	private JDBCConnectionManager() {}
	
	public static synchronized JDBCConnectionManager getInstance() {
		if (connectionManager == null) {
			connectionManager = new JDBCConnectionManager();
		}		
		return connectionManager;
	}

	@Override
	public synchronized Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
			return null;
		}
	}	
	
	@Override
	public synchronized void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException exc) {
				if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
			}
		}
	}
	
	@Override
	public synchronized void close(PreparedStatement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException exc) {
				if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
			}
		}
	}
	
	@Override
	public synchronized void close(ResultSet result) {
		if (result != null) {
			try {
				result.close();
			} catch (SQLException exc) {
				if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
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
