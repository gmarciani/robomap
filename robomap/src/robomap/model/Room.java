package robomap.model;

import java.util.ArrayList;
import java.util.List;

import robomap.model.Object;

public class Room {
	
	private String name; 
	private Dimension dimension;
	private Location location;	
	private List<Object> objects = new ArrayList<Object>();
	
	public Room(String name, Dimension dimension, Location location, List<Object> objects) {
		this.setName(name); 
		this.setDimension(dimension);
		this.setLocation(location);		
		this.setObjects(objects);
	}
	
	public Room(String name, Dimension dimension, Location location) {
		this.setName(name); 		
		this.setDimension(dimension);
		this.setLocation(location);
	}
	
	public Room() {
		
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

	public List<Object> getObjects() {
		return this.objects;
	}

	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}	
	
	public void addObject(Object object) {
		this.objects.add(object);
	}
	
	@Override
	public String toString() {
		return "Room(" + 
				this.getName() + ";" + 
				this.getDimension().toString() + ";" + 
				this.getLocation().toString() + ";" + 
				this.getObjects().toString() + ")";
	}

}
