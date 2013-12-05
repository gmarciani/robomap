package robomap.model.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import robomap.model.vector.Dimension;
import robomap.model.vector.Location;
import robomap.model.vector.Vector;

public class Home implements Serializable {
	
	private static final long serialVersionUID = -2389178962534385100L;
	
	private String name;
	private Dimension dimension;
	private Location start;
	private List<Room> rooms = new ArrayList<Room>();
	private List<Wall> walls = new ArrayList<Wall>();
	private List<Door> doors = new ArrayList<Door>();
	
	public Home(String name, Dimension dimension, Location start, List<Room> rooms, List<Wall> walls, List<Door> doors) {
		this.setName(name);
		this.setDimension(dimension);
		this.setStart(start);
		this.setRooms(rooms);
		this.setWalls(walls);
		this.setDoors(doors);
	}
	
	public Home(String name, Dimension dimension, List<Room> rooms) {
		this.setName(name);
		this.setDimension(dimension);
		this.setRooms(rooms);
	}
	
	public Home(String name, Dimension dimension, Location start) {
		this.setName(name);
		this.setDimension(dimension);
		this.setStart(start);
	}
	
	public Home(String name, Dimension dimension) {
		this.setName(name);
		this.setDimension(dimension);
	}
	
	public Home(String name) {
		this.setName(name);
	}
	
	public Home() {}

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
	
	public Location getStart() {
		return this.start;
	}

	public void setStart(Location start) {
		this.start = start;
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

	public List<Wall> getWalls() {
		return this.walls;
	}

	public void setWalls(List<Wall> walls) {
		this.walls = walls;
	}
	
	public void addWall(Wall wall) {
		this.walls.add(wall);
	}

	public List<Door> getDoors() {
		return this.doors;
	}

	public void setDoors(List<Door> doors) {
		this.doors = doors;
	}
	
	public void addDoor(Door door) {
		this.doors.add(door);
	}
	
	@Override
	public String toString() {
		return "Home(" + 
				this.getName() + ";" + 
				this.getDimension().toString() + ";" + 
				this.getStart().toString() + ";" +
				this.getRooms().toString() + ";" + 
				this.getWalls().toString() + ";" + 
				this.getDoors().toString() + ")";
	}

	public boolean comprehend(Location location) {
		Location homeLocation = new Location(0,0);
		return ((location.compareTo(homeLocation) == 1 ||  
				location.compareTo(homeLocation) == 0) &&
				(location.compareTo(Vector.sum(homeLocation, this.getDimension())) == -1 || 
				location.compareTo(Vector.sum(homeLocation, this.getDimension())) == 0));
	}

}
