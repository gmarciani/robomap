package robomap.database;

import robomap.model.home.Room;
import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.database
 *
 * @class RoomDAO
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public interface RoomDAO {
	
	public void saveRoom(String homeName, Room room);

	public Location getLocation(String name, String roomName);

	public Room getRoom(String name, String roomName);

}
