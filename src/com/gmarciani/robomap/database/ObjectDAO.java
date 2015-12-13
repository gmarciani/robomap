package robomap.database;

import java.util.List;

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

	public void saveObject(String homeName, Object object);

	public List<Object> getAllObjects(String homeName);	
	
	public Object getObject(String homeName, String objectName);

	public Object getObject(String homeName, String roomName, String objectName);

	public void setStatus(String homeName, String objectName, String status);

	public void setLocation(String homeName, String objectName, Location location);

	public Object getObject(String homeName, String objectName, Direction direction, String nearObjectName);

	

}
