package robomap.model.graph;

import java.io.Serializable;

import robomap.model.vector.Location;

public class Node implements Serializable {
	
	private static final long serialVersionUID = 784064600804020315L;
	
	private Location location;
	
	public Node(Location location) {
		this.setLocation(location);
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}	

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		 
        Node node = (Node) o;
 
        return this.getLocation().equals(node.getLocation());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return "Node(" + this.getLocation().toString() + ")";
	}

}
