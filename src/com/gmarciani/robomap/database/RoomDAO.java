package robomap.database;

import robomap.model.home.Room;

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

	public Room getRoom(String homeName, String roomName);

}
