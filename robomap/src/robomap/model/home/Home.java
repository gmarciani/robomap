package robomap.model.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import robomap.model.vector.Dimension;
import robomap.model.vector.Location;
import robomap.model.object.Object;

/**
 * @project robomap
 *
 * @package robomap.model.home
 *
 * @class Home
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class Home implements Serializable {
	
	private static final long serialVersionUID = -2389178962534385100L;
	
	private String name;
	private Dimension dimension;
	private Location start;
	private List<Room> rooms = new ArrayList<Room>();
	private List<Wall> walls = new ArrayList<Wall>();
	private List<Door> doors = new ArrayList<Door>();
	private List<Object> objects = new ArrayList<Object>();
	
	public Home(String name, Dimension dimension, Location start, List<Room> rooms, List<Wall> walls, List<Door> doors, List<Object> objects) {
		this.setName(name);
		this.setDimension(dimension);
		this.setStart(start);
		this.setRooms(rooms);
		this.setWalls(walls);
		this.setDoors(doors);
		this.setObjects(objects);
	}
	
	public Home(String name, Dimension dimension, Location start) {
		this.setName(name);
		this.setDimension(dimension);
		this.setStart(start);
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
	
	public List<Object> getObjects() {
		return this.objects;
	}

	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}
	
	public void addObject(Object object) {
		this.getObjects().add(object);
	}
	
	@Override
	public String toString() {
		return "Home(" + 
				this.getName() + ";" + 
				this.getDimension().toString() + ";" + 
				this.getStart().toString() + ";" +
				this.getRooms().toString() + ";" + 
				this.getWalls().toString() + ";" + 
				this.getDoors().toString() + ";" + 
				this.getObjects() + ")";
	}

	public boolean comprehend(Location location) {
		int locX = location.getX();
		int locY = location.getY();
		int homeX = 0;
		int homeY = 0;
		int homeWidth = this.getDimension().getWidth();
		int homeHeight = this.getDimension().getHeight();
		for (int ox = homeX; ox < homeX + homeWidth; ox ++) {
			for (int oy = homeY; oy < homeY + homeHeight; oy ++) {
				if (locX == ox && locY == oy) return true;
			}
		}
		return false;
	}

}
