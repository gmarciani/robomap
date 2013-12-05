package robomap.model.home;

import java.io.Serializable;

import robomap.model.vector.Direction;
import robomap.model.vector.Location;

public class Door implements Serializable {
	
	private static final long serialVersionUID = -3076339254360310436L;	
	
	private Location location;	
	private Direction direction;
	private int lenght;
	
	public Door(Location location, Direction direction, int lenght) {
		this.setLocation(location);
		this.setDirection(direction);
		this.setLenght(lenght);
		
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getLenght() {
		return this.lenght;
	}

	public void setLenght(int lenght) {
		this.lenght = lenght;
	}
	
	@Override
	public String toString() {
		return "Door(" + 
				this.getLocation() + ", " + 
				this.getDirection() + ", " + 
				this.getLenght() + ")";
	}

}
