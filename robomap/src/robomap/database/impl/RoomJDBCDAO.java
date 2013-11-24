package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.database.RoomDAO;
import robomap.log.Log;
import robomap.model.Room;

public class RoomJDBCDAO implements RoomDAO {
	
	private static ConnectionManager connectionManager;
	private static RoomDAO roomDAO;
	
	private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS room ("
			+ "id INT UNSIGNED NOT NULL AUTO_INCREMENT,"
			+ "name VARCHAR(20) NOT NULL,"
			+ "width INT UNSIGNED NOT NULL,"
			+ "height INT UNSIGNED NOT NULL,"
			+ "home INT UNSIGNED NOT NULL,"
			+ "CONSTRAINT pkRoom PRIMARY KEY (id),"
			+ "CONSTRAINT fkHome FOREIGN KEY (home) REFERENCES home(id) ON DELETE CASCADE)";
	
	private static final String SQL_INSERT = "";
	
	private static final String SQL_UPDATE = "";
	
	private static final String SQL_DELETE = "";
	
	private RoomJDBCDAO() {
		this.createTable();
	}	

	public static RoomDAO getInstance() {
		if(roomDAO == null) {
			roomDAO = new RoomJDBCDAO();
		}
		return roomDAO;
	}
	
	private void createTable() {
		connectionManager = JDBCConnectionManager.getInstance();	
		
		Connection connection = connectionManager.getConnection();
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement(SQL_CREATE_TABLE);
			statement.executeUpdate();
		} catch (SQLException exc) {
			Log.printSQLException("RoomJDBCDAO", "createTable", exc);
		} finally {
			connectionManager.close(connection, statement);
		}	
	}

	@Override
	public void saveRoom(String homeName, Room room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRoom(String homeName, Room room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRoom(String homeName, Room room) {
		// TODO Auto-generated method stub
		
	}

}
