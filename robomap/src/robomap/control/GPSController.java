package robomap.control;

import robomap.model.vector.Location;
import robomap.model.vector.Movement;

/**
 * @project robomap
 *
 * @package robomap.control
 *
 * @class GPSController
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class GPSController {
		
	private Location location;
	
	public GPSController() {}	

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public void setLocation(Movement movement) {
		Location newLocation = Location.computeLocation(this.getLocation(), movement);
		this.setLocation(newLocation);
	}

}
