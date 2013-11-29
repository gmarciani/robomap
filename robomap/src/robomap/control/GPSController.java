package robomap.control;

import robomap.model.base.Location;
import robomap.model.robot.Movement;

public class GPSController {
		
	private Location location;
	
	public GPSController() {

	}	

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
