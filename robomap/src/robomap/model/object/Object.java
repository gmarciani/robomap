package robomap.model.object;

import java.io.Serializable;

import robomap.model.vector.Dimension;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;
import robomap.model.vector.Vector;

public class Object implements Serializable {
	
	private static final long serialVersionUID = -8604922613106336953L;
	
	private String name;
	private Dimension dimension;
	private Location location;
	private Direction orientation;
	private String status;
	
	public Object(String name, Dimension dimension, Location location, Direction orientation, String status) {
		this.setName(name);
		this.setDimension(dimension);
		this.setLocation(location);
		this.setOrientation(orientation);
		this.setStatus(status);
	}
	
	public Object(String name, Dimension dimension, Location location, Direction orientation) {
		this.setName(name);
		this.setDimension(dimension);
		this.setLocation(location);
		this.setOrientation(orientation);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public Direction getOrientation() {
		return this.orientation;
	}

	public void setOrientation(Direction orientation) {
		this.orientation = orientation;
	}	

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Object(" + 
				this.getName() + ";" + 
				this.getDimension().toString() + ";" + 
				this.getLocation().toString() + ";" + 
				this.getStatus() + ")";
	}
	
	public Location getMiddleLocation() {
		int middleX = this.getLocation().getX() + (this.getDimension().getWidth() / 2);
		int middleY = this.getLocation().getY() + (this.getDimension().getHeight() / 2); 
		return new Location(middleX, middleY);
	}
	
	public boolean comprehend(Location location) {
		return ((location.compareTo(this.getLocation()) == 1 ||  
				location.compareTo(this.getLocation()) == 0) &&
				(location.compareTo(Vector.sum(this.getLocation(), this.getDimension())) == -1 || 
				location.compareTo(Vector.sum(this.getLocation(), this.getDimension())) == 0));
	}

}
