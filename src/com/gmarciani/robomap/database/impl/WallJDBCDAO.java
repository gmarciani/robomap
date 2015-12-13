package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.database.DatabaseDebug;
import robomap.database.WallDAO;
import robomap.log.Log;
import robomap.model.home.Wall;

/**
 * @project robomap
 *
 * @package robomap.database.impl
 *
 * @class WallJDBCDAO
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
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
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			stmt.executeUpdate();
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);
		}		
	}

}
