package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import robomap.database.ActionDAO;
import robomap.database.ConnectionManager;
import robomap.log.Log;
import robomap.model.object.Action;

public class ActionJDBCDAO implements ActionDAO {
	
	private static ConnectionManager connectionManager;
	private static ActionJDBCDAO actionDAO;
	
	private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS object ("
			+ "id INT UNSIGNED NOT NULL AUTO_INCREMENT,"
			+ "name VARCHAR(20) NOT NULL,"
			+ "x INT UNSIGNED NOT NULL,"
			+ "y INT UNSIGNED NOT NULL,"
			+ "width INT UNSIGNED NOT NULL,"
			+ "height INT UNSIGNED NOT NULL,"
			+ "room INT UNSIGNED NOT NULL,"
			+ "CONSTRAINT pkObject PRIMARY KEY (id),"
			+ "CONSTRAINT fkRoom FOREIGN KEY (room) REFERENCES room(id) ON DELETE CASCADE)";
	
	private static final String SQL_INSERT = "";
	
	private static final String SQL_UPDATE = "";
	
	private static final String SQL_DELETE = "";
	
	private ActionJDBCDAO() {
		this.createTable();
	}	
	
	public static ActionJDBCDAO getInstance() {
		if(actionDAO == null) {
			actionDAO = new ActionJDBCDAO();
		}
		return actionDAO;
	}
	
	private void createTable() {
		connectionManager = JDBCConnectionManager.getInstance();	
		
		Connection connection = connectionManager.getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement(SQL_CREATE_TABLE);
			statement.executeUpdate();
		} catch (SQLException exc) {
			Log.printSQLException("ObjectJDBCDAO", "createTable", exc);
		} finally {
			connectionManager.close(connection, statement);
		}	
	}

	@Override
	public void saveAction(Action action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAction(Action action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAction(Action action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Action getAction(String actionName) {
		// TODO Auto-generated method stub
		return null;
	}

}
