package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import robomap.database.ConnectionManager;
import robomap.database.DoorDAO;
import robomap.database.HomeDAO;
import robomap.database.ObjectDAO;
import robomap.database.RoomDAO;
import robomap.database.WallDAO;
import robomap.log.Log;
import robomap.model.Dimension;
import robomap.model.Door;
import robomap.model.Home;
import robomap.model.Room;
import robomap.model.Wall;

public class HomeJDBCDAO implements HomeDAO {
	
	private static ConnectionManager connectionManager;
	private static HomeDAO homeDAO;
	private static RoomDAO roomDAO;
	private static WallDAO wallDAO;
	private static DoorDAO doorDAO;
	
	private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS home ("
			+ "id INT UNSIGNED NOT NULL AUTO_INCREMENT,"
			+ "name VARCHAR(20) NOT NULL,"
			+ "width INT UNSIGNED NOT NULL,"
			+ "height INT UNSIGNED NOT NULL,"
			+ "CONSTRAINT pkHome PRIMARY KEY (id))";
	
	private static final String SQL_INSERT = "";
	
	private static final String SQL_UPDATE = "";
	
	private static final String SQL_DELETE = "";
	
	private HomeJDBCDAO() {
		this.createTable();
	}	

	public static HomeDAO getInstance() {
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
	public void saveHome(Home home) {
		String homeName = home.getName();
		Dimension homeDimension = home.getDimension();
		List<Room> rooms = home.getRooms();
		List<Wall> walls = home.getWalls();
		List<Door> doors = home.getDoors();
		
		for (Room room : rooms) {
			roomDAO.saveRoom(homeName, room);
		}
		
		for (Wall wall : walls) {
			wallDAO.saveWall(homeName, wall);
		}
		
		for (Door door : doors) {
			doorDAO.saveDoor(homeName, door);
		}
		
	}

	@Override
	public void updateHome(Home home) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteHome(Home home) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Home findHome(String homeName) {
		// TODO Auto-generated method stub
		return null;
	}

}
