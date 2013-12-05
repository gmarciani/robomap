package robomap.database;


import robomap.model.home.Door;

public interface DoorDAO {
	
	public void saveDoor(String homeName, Door door);

}
