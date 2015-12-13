package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import robomap.database.ConnectionManager;
import robomap.database.DatabaseDebug;
import robomap.database.InteractionDAO;
import robomap.database.ObjectDAO;
import robomap.log.Log;
import robomap.model.object.Interaction;
import robomap.model.object.Object;
import robomap.model.vector.Dimension;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.database.impl
 *
 * @class ObjectJDBCDAO
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class ObjectJDBCDAO implements ObjectDAO {
	
	private static ObjectJDBCDAO objectDAO;
	
	private InteractionDAO interactionDAO;
	
	private ConnectionManager connectionManager;	
	
	private static final String SQL_INSERT_OBJECT_IN_HOME = "INSERT INTO ObjectIn (hname, oname, ox, oy, owidth, oheight, oorientation, ostatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_SELECT_ALL_OBJECTS = "SELECT ox, oy, owidth, oheight, oorientation, ostatus FROM ObjectIn WHERE hname = ?";
	
	private static final String SQL_SELECT_OBJECT_BY_NAME = "SELECT ox, oy, owidth, oheight, oorientation, ostatus FROM ObjectIn WHERE (hname = ? AND oname = ?)";
	
	private static final String SQL_SELECT_OBJECT_BY_NAME_AND_ROOM = "SELECT ox, oy, owidth, oheight, oorientation, ostatus FROM ObjectIn WHERE (hname = ? AND rname = ? AND oname = ?)";
	
	private static final String SQL_UPDATE_OBJECT_STATUS_BY_NAME = "UPDATE ObjectIn SET ostatus = ? WHERE (hname = ? AND oname = ?)";
	
	private static final String SQL_UPDATE_OBJECT_LOCATION_BY_NAME = "UPDATE ObjectIn SET ox = ?, oy = ? WHERE (hname = ? AND oname = ?)";
	
	private static final String SQL_SELECT_OBJECT_NEAR_OBJECT = "SELECT OI2.ox, OI2.oy, OI2.owidth, OI2.oheight, OI2.oorientation, OI2.ostatus FROM ObjectIn AS OI2 "
			+ "WHERE OI2.hname = ? AND OI2.oname = ? AND EXISTS (SELECT * FROM NearObject AS NEO WHERE NEO.direction = ? AND NEO.oinid_b = OI2.oinid AND NEO.oinid_a = (SELECT oinid FROM ObjectIn AS OI1 WHERE OI1.oname = ?));";
	
	private ObjectJDBCDAO() {
		this.connectionManager = JDBCConnectionManager.getInstance();
		this.interactionDAO = InteractionJDBCDAO.getInstance();
	}	
	
	public static ObjectJDBCDAO getInstance() {
		if(objectDAO == null) {
			objectDAO = new ObjectJDBCDAO();
		}
		return objectDAO;
	}

	@Override
	public synchronized void saveObject(String homeName, Object object) {	
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(SQL_INSERT_OBJECT_IN_HOME);
			stmt.setString(1, homeName);
			stmt.setString(2, object.getName());
			stmt.setInt(3, object.getLocation().getX());
			stmt.setInt(4, object.getLocation().getY());
			stmt.setInt(5, object.getDimension().getWidth());
			stmt.setInt(6, object.getDimension().getHeight());
			stmt.setString(7, object.getOrientation().name());
			stmt.setString(8, object.getStatus());
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			stmt.executeUpdate();
			for (Interaction interaction : object.getInteractions()) {
				this.interactionDAO.saveInteraction(object.getName(), interaction);
			}
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);
		}	
	}

	@Override
	public List<Object> getAllObjects(String homeName) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		List<Object> objects = new ArrayList<Object>();
		try {
			stmt = connection.prepareStatement(SQL_SELECT_ALL_OBJECTS);
			stmt.setString(1, homeName);
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int x = rs.getInt(1);
				int y = rs.getInt(2);
				int width = rs.getInt(3);
				int height = rs.getInt(4);
				Object object = new Object(new Dimension(width, height), new Location(x, y));
				objects.add(object);
			}
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);
		}
		
		return objects;	
	}
	
	@Override
	public Object getObject(String homeName, String objectName) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		Object object = null;
		try {
			stmt = connection.prepareStatement(SQL_SELECT_OBJECT_BY_NAME);
			stmt.setString(1, homeName);
			stmt.setString(2, objectName);
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			ResultSet rs = stmt.executeQuery();
			if (rs.first()) {
				int x = rs.getInt(1);
				int y = rs.getInt(2);
				int width = rs.getInt(3);
				int height = rs.getInt(4);
				Direction orientation = Direction.valueOf(rs.getString(5));
				String status = rs.getString(6);
				object = new Object(objectName, new Dimension(width, height), new Location(x, y), orientation, status);
			}			
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);
		}
		
		return object;	
	}

	@Override
	public Object getObject(String homeName, String roomName, String objectName) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		Object object = null;
		try {
			stmt = connection.prepareStatement(SQL_SELECT_OBJECT_BY_NAME_AND_ROOM);
			stmt.setString(1, homeName);
			stmt.setString(2, roomName);
			stmt.setString(3, objectName);
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			ResultSet rs = stmt.executeQuery();
			if (rs.first()) {
				int x = rs.getInt(1);
				int y = rs.getInt(2);
				int width = rs.getInt(3);
				int height = rs.getInt(4);
				Direction orientation = Direction.valueOf(rs.getString(5));
				String status = rs.getString(6);
				object = new Object(objectName, new Dimension(width, height), new Location(x, y), orientation, status);
			}			
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);
		}
		
		return object;	
	}

	@Override
	public void setStatus(String homeName, String objectName, String status) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(SQL_UPDATE_OBJECT_STATUS_BY_NAME);
			stmt.setString(1, status);
			stmt.setString(2, homeName);
			stmt.setString(3, objectName);			
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			stmt.executeUpdate();
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);
		}
	}

	@Override
	public void setLocation(String homeName, String objectName, Location location) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(SQL_UPDATE_OBJECT_LOCATION_BY_NAME);			
			stmt.setInt(1, location.getX());
			stmt.setInt(2, location.getY());
			stmt.setString(3, homeName);
			stmt.setString(4, objectName);
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			stmt.executeUpdate();
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);
		}
	}

	@Override
	public Object getObject(String homeName, String objectName, Direction direction, String nearObjectName) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		Object object = null;
		try {
			stmt = connection.prepareStatement(SQL_SELECT_OBJECT_NEAR_OBJECT);
			stmt.setString(1, homeName);
			stmt.setString(2, objectName);
			stmt.setString(3, direction.getName());
			stmt.setString(4, nearObjectName);
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			ResultSet rs = stmt.executeQuery();
			if (rs.first()) {
				int x = rs.getInt(1);
				int y = rs.getInt(2);
				int width = rs.getInt(3);
				int height = rs.getInt(4);
				Direction orientation = Direction.valueOf(rs.getString(5));
				String status = rs.getString(6);
				object = new Object(objectName, new Dimension(width, height), new Location(x, y), orientation, status);
			}			
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);
		}
		
		return object;	
	}

	
}
