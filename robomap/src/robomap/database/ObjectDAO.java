package robomap.database;

import java.util.List;

import robomap.model.object.Interaction;
import robomap.model.object.Object;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.database
 *
 * @class ObjectDAO
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public interface ObjectDAO {
	
	public void saveObject(Object object);

	public void saveObject(String homeName, Object object);

	public List<Object> getAllObjects(String homeName);
	

	public Object getObject(String homeName, String roomName, String objectName);

	public Location getLocation(String homeName, String roomName, String objectName);

	public Location getLocation(String homeName, String roomName, String objectName, Interaction action);

	public void setOrientation(String homeName, String roomName, String objectName, Direction orientation);

	public void setStatus(Object object, String status);

	public void setLocation(Object object, Location location);

}
