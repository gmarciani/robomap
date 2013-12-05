package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.database.WallDAO;
import robomap.log.Log;
import robomap.model.home.Wall;
import robomap.model.vector.Location;

public class WallJDBCDAO implements WallDAO {
	
	private static WallDAO wallDAO;
	
	private ConnectionManager connectionManager;
	
	private static final String SQL_INSERT = "INSERT INTO Wall (hname, wx, wy, worientation, wlenght) VALUES (?, ?, ?, ?, ?)";
	
	private WallJDBCDAO() {
		this.connectionManager = JDBCConnectionManager.getInstance();
	}
	
	public static WallDAO getInstance() {
		if (wallDAO == null) {
			wallDAO = new WallJDBCDAO();
		}
		return wallDAO;
	}

	@Override
	public synchronized void saveWall(String homeName, Wall wall) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(SQL_INSERT);
			stmt.setString(1, homeName);
			stmt.setInt(2, wall.getLocation().getX());
			stmt.setInt(3, wall.getLocation().getY());
			stmt.setString(4, wall.getDirection().name());
			stmt.setInt(5, wall.getLenght());
			stmt.executeUpdate();
		} catch (SQLException exc) {
			Log.printSQLException("WallJDBCDAO", "saveWall", exc);
		} finally {
			this.connectionManager.close(stmt);
		}		
	}

	@Override
	public boolean isThereAny(Location locationA, Location locationB) {
		// TODO Auto-generated method stub
		return false;
	}

}
