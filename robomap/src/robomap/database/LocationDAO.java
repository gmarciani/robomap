package robomap.database;

import robomap.model.home.Home;
import robomap.model.vector.Location;

public interface LocationDAO {

	boolean isLocked(Home currentHome, Location location);

	void lock(Home currentHome, Location location);

	void unlock(Home currentHome, Location location);

}
