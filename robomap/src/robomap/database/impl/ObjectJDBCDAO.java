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
	
	private static final String SQL_INSERT_OBJECT = "INSERT INTO Object (oname) VALUES (?)";
	
	private static final String SQL_INSERT_OBJECT_IN_HOME = "INSERT INTO ObjectIn (hname, oname, ox, oy, owidth, oheight, oorientation, ostatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_SELECT_ALL_OBJECTS = "SELECT ox, oy, owidth, oheight FROM (SELECT * FROM ObjectIn WHERE hname = ?) AS OI JOIN Object";
	
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
	public synchronized void saveObject(Object object) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(SQL_INSERT_OBJECT);
			stmt.setString(1, object.getName());
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
	public synchronized void saveObject(String homeName, Object object) {	
		this.saveObject(object);
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
	public Object getObject(String name, String roomName, String objectName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocation(String name, String roomName, String objectName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocation(String name, String roomName,
			String objectName, Interaction action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOrientation(String name, String roomName, String objectName,
			Direction orientation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatus(Object object, String status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocation(Object object, Location location) {
		// TODO Auto-generated method stub
		
	}
}
