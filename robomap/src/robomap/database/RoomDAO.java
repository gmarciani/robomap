package robomap.database;


import robomap.model.home.Room;
import robomap.model.vector.Location;

public interface RoomDAO {
	
	public void saveRoom(String homeName, Room room);

	public Location getLocation(String name, String roomName);

}
