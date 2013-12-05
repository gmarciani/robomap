package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.database.ObjectDAO;
import robomap.database.RoomDAO;
import robomap.log.Log;
import robomap.model.home.Room;
import robomap.model.object.Object;
import robomap.model.vector.Location;

public class RoomJDBCDAO implements RoomDAO {
	
	private static RoomDAO roomDAO;
	
	private ConnectionManager connectionManager;
	private ObjectDAO objectDAO;
	
	private static final String SQL_INSERT = "INSERT INTO Room (hname, rname, rx, ry, rwidth, rheight) VALUES (?, ?, ?, ?, ?, ?)";
	
	private RoomJDBCDAO() {
		this.connectionManager = JDBCConnectionManager.getInstance();
		objectDAO = ObjectJDBCDAO.getInstance();
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
			stmt.executeUpdate();
		} catch (SQLException exc) {
			Log.printSQLException("RoomJDBCDAO", "saveRoom", exc);
		} finally {
			this.connectionManager.close(stmt);
		}			
		
		for (Object object : room.getObjects()) {
			this.objectDAO.saveObject(homeName, room.getName(), object);
		}			
	}

	@Override
	public Location getLocation(String name, String roomName) {
		// TODO Auto-generated method stub
		return null;
	}

}
