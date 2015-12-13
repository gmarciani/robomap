package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.database.DatabaseDebug;
import robomap.database.LockDAO;
import robomap.log.Log;
import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.database.impl
 *
 * @class LockJDBCDAO
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class LockJDBCDAO implements LockDAO {
	
	private static LockJDBCDAO lockDAO;
	
	private ConnectionManager connectionManager;	
	
	private static final String SQL_DELETE_ALL_LOCKS = "DELETE FROM RobotLock WHERE robot = ?";
	
	private static final String SQL_INSERT_LOCK = "INSERT INTO RobotLock (hname, robot, lx, ly) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE hname = VALUES(hname), lx = VALUES(lx), ly = VALUES(ly)";
	
	private static final String SQL_IS_LOCK = "SELECT COUNT(*) FROM RobotLock WHERE (hname = ? AND robot != ? AND lx = ? AND ly = ?)";
	
	private LockJDBCDAO() {
		this.connectionManager = JDBCConnectionManager.getInstance();
	}	
	
	public static LockJDBCDAO getInstance() {
		if(lockDAO == null) {
			lockDAO = new LockJDBCDAO();
		}
		return lockDAO;
	}

	@Override
	public synchronized void deleteLock(String robotName) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(SQL_DELETE_ALL_LOCKS);
			stmt.setString(1, robotName);
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			stmt.executeUpdate();
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);	
		}
	}

	@Override
	public synchronized void lock(String homeName, String robotName, Location location) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(SQL_INSERT_LOCK);
			stmt.setString(1, homeName);
			stmt.setString(2, robotName);
			stmt.setInt(3, location.getX());
			stmt.setInt(4, location.getY());
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			stmt.executeUpdate();
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);	
		}
	}

	@Override
	public synchronized boolean isLock(String homeName, String robotName, Location location) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		boolean isLock = false;
		try {
			stmt = connection.prepareStatement(SQL_IS_LOCK);
			stmt.setString(1, homeName);
			stmt.setString(2, robotName);
			stmt.setInt(3, location.getX());
			stmt.setInt(4, location.getY());
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			ResultSet rs = stmt.executeQuery();
			if (rs.first()) {
				isLock = (rs.getInt(1) > 0)? true : false;
			}
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);	
		}
		return isLock;
	}
	
}
