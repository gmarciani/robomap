package robomap.model.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import robomap.model.vector.Dimension;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.model.object
 *
 * @class Object
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class Object implements Serializable {
	
	private static final long serialVersionUID = -8604922613106336953L;
	
	private String name;
	private Dimension dimension;
	private Location location;
	private Direction orientation;
	private List<Interaction> interactions = new ArrayList<Interaction>();
	private String status;
	
	public Object(String name, Dimension dimension, Location location, Direction orientation, List<Interaction> interactions, String status) {
		this.setName(name);
		this.setDimension(dimension);
		this.setLocation(location);
		this.setOrientation(orientation);
		this.setInteractions(interactions);
		this.setStatus(status);
	}
	
	public Object(String name, Dimension dimension, Location location, Direction orientation, String status) {
		this.setName(name);
		this.setDimension(dimension);
		this.setLocation(location);
		this.setOrientation(orientation);
		this.setStatus(status);
	}
	
	public Object(String name, Dimension dimension, Location location) {
		this.setName(name);
		this.setDimension(dimension);
		this.setLocation(location);
	}
	
	public Object(Dimension dimension, Location location) {
		this.setDimension(dimension);
		this.setLocation(location);
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
	
	public List<Interaction> getInteractions() {
		return this.interactions;
	}

	public void setInteractions(List<Interaction> interactions) {
		this.interactions = interactions;
	}
	
	public void addInteraction(Interaction interaction) {
		this.getInteractions().add(interaction);
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
				this.getStatus() + ";" + 
				this.getInteractions() + ")";
	}
	
	public Location getMiddleLocation() {
		int middleX = this.getLocation().getX() + (this.getDimension().getWidth() / 2);
		int middleY = this.getLocation().getY() + (this.getDimension().getHeight() / 2); 
		return new Location(middleX, middleY);
	}
	
	public boolean comprehend(Location location) {
		int locX = location.getX();
		int locY = location.getY();
		int objX = this.getLocation().getX();
		int objY = this.getLocation().getY();
		int objWidth = this.getDimension().getWidth();
		int objHeight = this.getDimension().getHeight();
		for (int ox = objX; ox < objX + objWidth; ox ++) {
			for (int oy = objY; oy < objY + objHeight; oy ++) {
				if (locX == ox && locY == oy) return true;
			}
		}
		return false;
	}

	public List<Location> getCoveredLocations() {
		List<Location> coveredLocations = new ArrayList<Location>();
		int objX = this.getLocation().getX();
		int objY = this.getLocation().getY();
		int objWidth = this.getDimension().getWidth();
		int objHeight = this.getDimension().getHeight();
		for (int x = objX; x < objX + objWidth; x ++) {
			for (int y = objY; y < objY + objHeight; y ++) {
				Location location = new Location(x, y);
				coveredLocations.add(location);
			}
		}
		return coveredLocations;
	}
	
	public Location getObjectEdge(Direction direction) {
		Location location = this.getMiddleLocation();
		while(this.comprehend(location)) {
			location = Location.computeLocation(location, this.getOrientation(), direction, 1);
		}
		return location;
	}

}
