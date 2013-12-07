package robomap.database;

import robomap.model.home.Wall;

/**
 * @project robomap
 *
 * @package robomap.database
 *
 * @class WallDAO
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public interface WallDAO {
	
	public void saveWall(String homeName, Wall wall);

}
