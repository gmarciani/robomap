package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import robomap.database.ConnectionManager;
import robomap.database.ObjectDAO;
import robomap.log.Log;
import robomap.model.base.Direction;
import robomap.model.base.Location;
import robomap.model.object.Action;
import robomap.model.object.Object;

public class ObjectJDBCDAO implements ObjectDAO {
	
	private static ConnectionManager connectionManager = null;
	private static ObjectJDBCDAO objectDAO = null;
	
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
	
	private ObjectJDBCDAO() {
		this.createTable();
	}	
	
	public static ObjectJDBCDAO getInstance() {
		if(objectDAO == null) {
			objectDAO = new ObjectJDBCDAO();
		}
		return objectDAO;
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
	public void saveObject(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateObject(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteObject(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getActions(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeStatus(Object object, String newStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Location getLocation(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocation(Object object, Action action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocation(Object object, Direction direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocation(Object payload, Location currentLocation) {
		// TODO Auto-generated method stub
		
	}

}
