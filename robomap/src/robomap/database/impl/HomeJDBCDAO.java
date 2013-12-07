package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import robomap.control.DisplayController;
import robomap.database.ConnectionManager;
import robomap.database.DatabaseDebug;
import robomap.database.DoorDAO;
import robomap.database.HomeDAO;
import robomap.database.ObjectDAO;
import robomap.database.RoomDAO;
import robomap.database.WallDAO;
import robomap.log.Log;
import robomap.model.home.Door;
import robomap.model.home.Home;
import robomap.model.home.Room;
import robomap.model.home.Wall;
import robomap.model.object.Object;

/**
 * @project robomap
 *
 * @package robomap.database.impl
 *
 * @class HomeJDBCDAO
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class HomeJDBCDAO implements HomeDAO {
	
	private static HomeDAO homeDAO;
	
	private ConnectionManager connectionManager;	
	private RoomDAO roomDAO;
	private WallDAO wallDAO;
	private DoorDAO doorDAO;
	private ObjectDAO objectDAO;
	
	private static final String SQL_INSERT = "INSERT INTO Home (hname, hwidth, hheight, hstart_x, hstart_y) VALUES (?, ?, ?, ?, ?)";
	
	private HomeJDBCDAO() {
		this.connectionManager = JDBCConnectionManager.getInstance();
		this.roomDAO = RoomJDBCDAO.getInstance();
		this.wallDAO = WallJDBCDAO.getInstance();
		this.doorDAO = DoorJDBCDAO.getInstance();
		this.objectDAO = ObjectJDBCDAO.getInstance();
	}	

	public static HomeDAO getInstance() {
		if(homeDAO == null) {
			homeDAO = new HomeJDBCDAO();
		}
		return homeDAO;
	}

	@Override
	public synchronized void saveHome(Home home) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(SQL_INSERT);
			stmt.setString(1, home.getName());
			stmt.setInt(2, home.getDimension().getWidth());
			stmt.setInt(3, home.getDimension().getHeight());
			stmt.setInt(4, home.getStart().getX());
			stmt.setInt(5, home.getStart().getY());
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			stmt.executeUpdate();
		} catch (SQLException exc) {
			DisplayController.getInstance().showError(exc.getMessage());
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
			if (exc.getErrorCode() == 1062) return;
		} finally {
			this.connectionManager.close(connection);
		}			
		
		for (Wall wall : home.getWalls()) {
			this.wallDAO.saveWall(home.getName(), wall);
		}
		
		for (Door door : home.getDoors()) {
			this.doorDAO.saveDoor(home.getName(), door);
		}
		
		for (Room room : home.getRooms()) {
			this.roomDAO.saveRoom(home.getName(), room);
		}
		
		for (Object object : home.getObjects()) {
			this.objectDAO.saveObject(home.getName(), object);
		}
	}

}
