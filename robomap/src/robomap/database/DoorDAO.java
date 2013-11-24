package robomap.database;

import robomap.model.Door;

public interface DoorDAO {
	
	public void saveDoor(String homeName, Door door);
	
	public void updateDoor(String homeName, Door door);
	
	public void deleteDoor(String homeName, Door door);

}
