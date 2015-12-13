package robomap.database;

import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.database
 *
 * @class LockDAO
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public interface LockDAO {

	void deleteLock(String robotName);

	void lock(String homeName, String robotName, Location location);

	boolean isLock(String homeName, String robotName, Location location);

}
