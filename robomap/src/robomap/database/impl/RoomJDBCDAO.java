package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.database.DatabaseDebug;
import robomap.database.RoomDAO;
import robomap.log.Log;
import robomap.model.home.Room;
import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.database.impl
 *
 * @class RoomJDBCDAO
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class RoomJDBCDAO implements RoomDAO {
	
	private static RoomDAO roomDAO;
	
	private ConnectionManager connectionManager;
	
	private static final String SQL_INSERT = "INSERT INTO Room (hname, rname, rx, ry, rwidth, rheight) VALUES (?, ?, ?, ?, ?, ?)";
	
	private RoomJDBCDAO() {
		this.connectionManager = JDBCConnectionManager.getInstance();
	}	

	public static RoomDAO getInstance() {
		if(roomDAO == null) {
			roomDAO = new RoomJDBCDAO();
		}
		return roomDAO;
	}

	@Override
	public synchronized void saveRoom(String homeName, Room room) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(SQL_INSERT);
			stmt.setString(1, homeName);
			stmt.setString(2, room.getName());
			stmt.setInt(3, room.getLocation().getX());
			stmt.setInt(4, room.getLocation().getY());
			stmt.setInt(5, room.getDimension().getWidth());
			stmt.setInt(6, room.getDimension().getHeight());
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			stmt.executeUpdate();
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);
		}			
	}

	@Override
	public Location getLocation(String name, String roomName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room getRoom(String name, String roomName) {
		// TODO Auto-generated method stub
		return null;
	}

}
