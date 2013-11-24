package robomap.model;

import java.util.ArrayList;
import java.util.List;

public class Wall {
	
	private Dimension dimension;
	private Location location;	
	private List<Room> rooms = new ArrayList<Room>();
	
	public Wall(Dimension dimension, Location location, List<Room> rooms) {
		this.setDimension(dimension);
		this.setLocation(location);
		this.setRooms(rooms);
	}
	
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

	public List<Room> getRooms() {
		return this.rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}	
	
	public void addRoom(Room room) {
		this.rooms.add(room);
	}

	@Override
	public String toString() {
		return "Wall(" + 
				this.getDimension().toString() + ";" + 
				this.getLocation().toString() + ")";
	}
}
