package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import robomap.database.ConnectionManager;
import robomap.database.LocationDAO;
import robomap.log.Log;
import robomap.model.home.Home;
import robomap.model.vector.Location;

public class LocationJDBCDAO implements LocationDAO {
	
	private static ConnectionManager connectionManager;
	private static LocationDAO locationDAO;
	
	private LocationJDBCDAO() {}	

	public static LocationDAO getInstance() {
		if(locationDAO == null) {
			locationDAO = new LocationJDBCDAO();
		}
		return locationDAO;
	}

	@Override
	public boolean isLocked(Home currentHome, Location location) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lock(Home currentHome, Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unlock(Home currentHome, Location location) {
		// TODO Auto-generated method stub
		
	}
	
}
