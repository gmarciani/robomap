package robomap.model.vector;

import java.io.Serializable;

/**
 * @project robomap
 *
 * @package robomap.model.vector
 *
 * @class Location
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class Location extends Vector implements Serializable {

	private static final long serialVersionUID = -9033935801147702920L;
	
	public Location(int x, int y) {
		super(x, y);
	}	
	
	@Override
    public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		 
        Location location = (Location) o;
 
        return this.getX() == location.getX() && this.getY() == location.getY();
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.getX();
		result = prime * result + this.getY();
		return result;
	}
	
	@Override
	public String toString() {
		return "Location(" + 
				this.getX() + ";" + 
				this.getY() + ")";
	}	
	
	public static Location computeLocation(Location location, Movement movement) {
		int currX = location.getX();
		int currY = location.getY();
		int moveX = (int) (movement.getModule() * movement.getDirection().getVersor().getX());
		int moveY = (int) (movement.getModule() * movement.getDirection().getVersor().getY());
		return new Location(currX + moveX, currY + moveY);
	}

	public static Location computeLocation(Location location, Direction orientation, Direction direction, int distance) {
		if (direction == Direction.NONE) return location;
		Vector rotatedVersor = Vector.rotate(orientation.getVersor(), direction.getRotation());
		int x = location.getX() + (distance * rotatedVersor.getX());
		int y = location.getY() + (distance * rotatedVersor.getY());
		return new Location(x, y);
	}

}
