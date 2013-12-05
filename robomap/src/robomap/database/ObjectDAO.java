package robomap.database;

import java.util.List;

import robomap.model.object.Action;
import robomap.model.object.Object;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;

public interface ObjectDAO {
	
	public int saveObject(Object object);

	public void saveObject(String homeName, String roomName, Object object);

	public boolean isThereAny(Location nextLocation);

	public Object getObject(String name, String roomName, String objectName);

	public Location getLocation(String name, String roomName, String objectName);

	public Location getLocation(String name, String roomName, String objectName, Action action);

	public void setOrientation(String name, String roomName, String objectName, Direction orientation);

	public List<String> getActions(Object object);

	public Location getLocation(Object object, Action action);

	public void setStatus(Object object, String status);

	public void setLocation(Object object, Location location);

}
