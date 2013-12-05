package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import robomap.database.ConnectionManager;
import robomap.database.ObjectDAO;
import robomap.log.Log;
import robomap.model.object.Action;
import robomap.model.object.Object;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;

public class ObjectJDBCDAO implements ObjectDAO {
	
	private static ObjectJDBCDAO objectDAO;
	
	private ConnectionManager connectionManager;	
	
	private static final String SQL_INSERT_OBJECT = "INSERT INTO Object (oname, owidth, oheight) VALUES (?, ?, ?)";
	
	private static final String SQL_INSERT_OBJECT_IN_ROOM = "INSERT INTO ObjectIn (hname, rname, oid, ox, oy, oorientation, ostatus) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_SELECT_OBJECT_ID = "SELECT oid FROM Object WHERE (oname = ? AND owidth = ? AND oheight = ?)";
	
	private ObjectJDBCDAO() {
		this.connectionManager = JDBCConnectionManager.getInstance();
	}	
	
	public static ObjectJDBCDAO getInstance() {
		if(objectDAO == null) {
			objectDAO = new ObjectJDBCDAO();
		}
		return objectDAO;
	}
	
	@Override
	public int saveObject(Object object) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		int id = -1;
		try {
			stmt = connection.prepareStatement(SQL_INSERT_OBJECT, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, object.getName());
			stmt.setInt(2, object.getDimension().getWidth());
			stmt.setInt(3, object.getDimension().getHeight());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) id = rs.getInt(1);
		} catch (SQLException exc) {
			Log.printSQLException("ObjectJDBCDAO", "saveObject", exc);
		} finally {
			this.connectionManager.close(stmt);	
		}
		
		return id;		
	}

	@Override
	public void saveObject(String homeName, String roomName, Object object) {		
		int objectId = this.getObjectId(object);
		
		if (objectId == -1) objectId = this.saveObject(object);
		
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(SQL_INSERT_OBJECT_IN_ROOM);
			stmt.setString(1, homeName);
			stmt.setString(2, roomName);
			stmt.setInt(3, objectId);
			stmt.setInt(4, object.getLocation().getX());
			stmt.setInt(5, object.getLocation().getY());
			stmt.setString(6, object.getOrientation().name());
			stmt.setString(7, object.getStatus());
			stmt.executeUpdate();
		} catch (SQLException exc) {
			Log.printSQLException("ObjectJDBCDAO", "saveObject", exc);
		} finally {
			this.connectionManager.close(stmt);
		}				
	}

	private int getObjectId(Object object) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		int id = -1;
		try {
			stmt = connection.prepareStatement(SQL_SELECT_OBJECT_ID);
			stmt.setString(1, object.getName());
			stmt.setInt(2, object.getDimension().getWidth());
			stmt.setInt(3, object.getDimension().getHeight());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) return rs.getInt("oid");	
		} catch (SQLException exc) {
			Log.printSQLException("ObjectJDBCDAO", "getObjectId", exc);
		} finally {
			this.connectionManager.close(stmt);
		}
		
		return id;			
	}

	@Override
	public boolean isThereAny(Location nextLocation) {
		// TODO Auto-generated method stub
		return false;
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
			String objectName, Action action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOrientation(String name, String roomName, String objectName,
			Direction orientation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getActions(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocation(Object object, Action action) {
		// TODO Auto-generated method stub
		return null;
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
