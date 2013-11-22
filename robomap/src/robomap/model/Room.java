package robomap.model;

import java.util.Set;

public class Room {
	
	private String name;
	private Location location;
	private Dimension dimension;
	private Set<Object> objects;
	
	public Room(String name, Location location, Dimension dimension, Set<Object> objects) {
		this.setName(name); 
		this.setLocation(location);
		this.setDimension(dimension);
		this.setObjects(objects);
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

	public Set<Object> getObjects() {
		return this.objects;
	}

	public void setObjects(Set<Object> objects) {
		this.objects = objects;
	}	

}
