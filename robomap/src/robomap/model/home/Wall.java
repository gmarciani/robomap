package robomap.model.home;

import java.io.Serializable;

import robomap.model.vector.Direction;
import robomap.model.vector.Location;

public class Wall implements Serializable {
	
	private static final long serialVersionUID = 6622368632337190338L;	
	
	private Location location;	
	private Direction direction;
	private int lenght;
	
	public Wall(Location location, Direction direction, int lenght) {
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
		return "Wall(" + 
				this.getLocation() + ", " + 
				this.getDirection() + ", " + 
				this.getLenght() + ")";
	}
	
}
