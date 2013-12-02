package robomap.model.home;

import java.io.Serializable;

import robomap.model.vector.Dimension;
import robomap.model.vector.Location;

public class Wall implements Serializable {
	
	private static final long serialVersionUID = 6622368632337190338L;
	
	private Dimension dimension;
	private Location location;	
	
	public Wall(Dimension dimension, Location location) {
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
		return "Wall(" + 
				this.getDimension().toString() + ";" + 
				this.getLocation().toString() + ")";
	}
}
