package robomap.model.graph;

import robomap.model.Location;

public class Node {
	
	private int id;
	private Location location;
	
	public Node(Location location) {
		this.setLocation(location);
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}	

}
