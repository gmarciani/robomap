package robomap.database;

import robomap.model.home.Door;

/**
 * @project robomap
 *
 * @package robomap.database
 *
 * @class DoorDAO
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public interface DoorDAO {
	
	public void saveDoor(String homeName, Door door);

}
