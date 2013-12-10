package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.database.DatabaseDebug;
import robomap.database.RoomDAO;
import robomap.log.Log;
import robomap.model.home.Room;
import robomap.model.vector.Dimension;
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
	
	private static final String SQL_SELECT_ROOM = "SELECT rx, ry, rwidth, rheight FROM Room WHERE hname = ? AND rname = ?";
	
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
	public Room getRoom(String homeName, String roomName) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		Room room = null;
		try {
			stmt = connection.prepareStatement(SQL_SELECT_ROOM);
			stmt.setString(1, homeName);
			stmt.setString(2, roomName);
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			ResultSet rs = stmt.executeQuery();
			if (rs.first()) {
				int x = rs.getInt(1);
				int y = rs.getInt(2);
				int width = rs.getInt(3);
				int height = rs.getInt(4);
				room = new Room(roomName, new Location(x, y), new Dimension(width, height));
			}
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);
		}
		
		return room;
	}

}
