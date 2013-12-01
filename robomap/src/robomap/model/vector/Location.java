package robomap.model.vector;

import java.io.Serializable;

public class Location extends Vector implements Serializable {

	private static final long serialVersionUID = -9033935801147702920L;
	
	public Location(int x, int y) {
		super(x, y);
	}
	
	public static Location computeLocation(Location location, Movement movement) {
		int currX = location.getX();
		int currY = location.getY();
		int moveX = (int) (movement.getModule() * movement.getDirection().getVersor().getX());
		int moveY = (int) (movement.getModule() * movement.getDirection().getVersor().getY());
		return new Location(currX + moveX, currY + moveY);
	}
	
	public static Location computeLocation(Location location, Direction orientation, Direction relativePosition) {
		int x = 0;
		int y = 0;
		return new Location(x, y);
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
	
	public static Location getRelativeLocation(Location location, Direction orientation, Direction relativePosition) {
		Vector rotatedVersor = Vector.rotate(orientation.getVersor(), relativePosition.getRotation());
		int x = location.getX() + rotatedVersor.getX();
		int y = location.getY() + rotatedVersor.getY();
		return new Location(x, y);
	}

}
