package robomap.model.home;

import java.io.Serializable;

import robomap.model.vector.Dimension;
import robomap.model.vector.Location;

public class Door implements Serializable {
	
	private static final long serialVersionUID = -3076339254360310436L;
	
	private Dimension dimension;
	private Location location;	
	
	public Door(Dimension dimension, Location location) {
		this.setLocation(location);
		this.setDimension(dimension);
	}
	
	public Dimension getDimension() {
		return this.dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return "Door(" + 
				this.getDimension().toString() + ";" + 
				this.getLocation().toString() + ")";
	}

}
