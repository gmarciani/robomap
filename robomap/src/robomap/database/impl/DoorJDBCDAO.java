package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.database.DoorDAO;
import robomap.log.Log;
import robomap.model.home.Door;

public class DoorJDBCDAO implements DoorDAO {
	
	private static DoorDAO doorDAO;
	
	private ConnectionManager connectionManager;	
	
	private static final String SQL_INSERT = "INSERT INTO Door (hname, dx, dy, dorientation, dlenght) VALUES (?, ?, ?, ?, ?);";
	
	private DoorJDBCDAO() {
		this.connectionManager = JDBCConnectionManager.getInstance();
	}
	
	public static DoorDAO getInstance() {
		if (doorDAO == null) {
			doorDAO = new DoorJDBCDAO();
		}
		return doorDAO;
	}

	@Override
	public synchronized void saveDoor(String homeName, Door door) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(SQL_INSERT);
			stmt.setString(1, homeName);
			stmt.setInt(2, door.getLocation().getX());
			stmt.setInt(3, door.getLocation().getY());
			stmt.setString(4, door.getDirection().name());
			stmt.setInt(5, door.getLenght());
			stmt.executeUpdate();
		} catch (SQLException exc) {
			Log.printSQLException("DoorJDBCDAO", "saveDoor", exc);
		} finally {
			this.connectionManager.close(stmt);	
		}		
			
	}
}
