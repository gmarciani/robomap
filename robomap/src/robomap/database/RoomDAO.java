package robomap.database;

import robomap.model.Room;

public interface RoomDAO {
	
	public void saveRoom(String homeName, Room room);
	
	public void updateRoom(String homeName, Room room);
	
	public void deleteRoom(String homeName, Room room);

}
