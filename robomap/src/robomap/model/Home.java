package robomap.model;

import java.util.List;

public class Home {
	
	private String name;
	private Dimension dimension;
	private List<Room> rooms;
	
	public Home(String name, Dimension dimension, List<Room> rooms) {
		this.setName(name);
		this.setDimension(dimension);
		this.setRooms(rooms);
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

	public List<Room> getRooms() {
		return this.rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}	

}
