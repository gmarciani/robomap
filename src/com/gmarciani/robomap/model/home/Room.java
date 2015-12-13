package robomap.model.home;

import java.io.Serializable;
import robomap.model.vector.Dimension;
import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.model.home
 *
 * @class Room
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class Room implements Serializable {

	private static final long serialVersionUID = 1474918982284933753L;
	
	private String name; 	
	private Location location;	
	private Dimension dimension;
	
	public Room(String name, Location location, Dimension dimension) {
		this.setName(name); 		
		this.setDimension(dimension);
		this.setLocation(location);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Dimension getDimension() {
		return this.dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	
	@Override
	public String toString() {
		return "Room(" + 
				this.getName() + ";" + 
				this.getDimension().toString() + ";" + 
				this.getLocation().toString() + ")";
	}
	
	public Location getMiddleLocation() {
		int middleX = this.getLocation().getX() + (this.getDimension().getWidth() / 2);
		int middleY = this.getLocation().getY() + (this.getDimension().getHeight() / 2); 
		return new Location(middleX, middleY);
	}

}
