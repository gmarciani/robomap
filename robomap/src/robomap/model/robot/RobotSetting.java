package robomap.model.robot;

import java.io.Serializable;

import robomap.model.base.Location;

public class RobotSetting implements Serializable {
	
	private static final long serialVersionUID = -7528580322033068828L;
	
	Location startLocation;
	
	public RobotSetting() {
		
	}
	
	
	public Location getStartLocation() {
		return this.startLocation;
	}
	
	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

}
