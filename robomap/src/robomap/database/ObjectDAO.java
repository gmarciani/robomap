package robomap.database;

import java.util.List;

import robomap.model.base.Direction;
import robomap.model.base.Location;
import robomap.model.object.Action;
import robomap.model.object.Object;

public interface ObjectDAO {

	public void saveObject(Object object);
	
	public void updateObject(Object object);
	
	public void deleteObject(Object object);

	public List<String> getActions(Object object);

	public void changeStatus(Object object, String newStatus);

	public Location getLocation(Object object);

	public Location getLocation(Object object, Action action);

	public Location getLocation(Object object, Direction direction);

	public void setLocation(Object payload, Location currentLocation);

}
