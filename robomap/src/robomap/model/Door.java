package robomap.model;

import java.util.List;

public class Door {
	
	private Location[] extension = new Location[2];
	private List<Room> rooms;
	
	public Door(Location[] extension, List<Room> rooms) {
		this.setExtension(extension);
		this.setRooms(rooms);
	}

	public Location[] getExtension() {
		return this.extension;
	}

	public void setExtension(Location[] extension) {
		this.extension = extension;
	}

	public List<Room> getRooms() {
		return this.rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

}
