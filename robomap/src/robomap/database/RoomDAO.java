package robomap.database;

import robomap.model.base.Location;
import robomap.model.home.Room;

public interface RoomDAO {
	
	public void saveRoom(String homeName, Room room);
	
	public void updateRoom(String homeName, Room room);
	
	public void deleteRoom(String homeName, Room room);

	public Location getLocation(Room room);

}
