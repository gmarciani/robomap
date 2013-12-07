package robomap.model.home;

import java.io.Serializable;

import robomap.model.vector.Direction;
import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.model.home
 *
 * @class Wall
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
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
	
	public boolean blocks(Location locationA, Location locationB) {		
		int wallX = this.getLocation().getX();
		int wallY = this.getLocation().getY();	
		Direction wallDirection = this.getDirection();
		int wallLenght = this.getLenght();
		
		int xA = locationA.getX();
		int yA = locationA.getY();
		int xB = locationB.getX();
		int yB = locationB.getY();
		
		if (wallDirection == Direction.RIGHT) {
			for (int wx = wallX; wx < wallX + wallLenght; wx ++) {
				if ((xA == wx && yA == wallY - 1 && yB == wallY && (xB == xA || xB == xA - 1 || xB == xA + 1)) 
						|| (xA == wx + 1) && yA == wallY - 1 && yB == wallY && xB == wx 
						|| (xA == wx - 1) && yA == wallY - 1 && yB == wallY && xB == wx
						|| (xA == wx && yA == wallY && yB == wallY - 1 && (xB == xA || xB == xA - 1 || xB == xA + 1))
						|| (xA == wx + 1) && yA == wallY && yB == wallY - 1 && xB == wx 
						|| (xA == wx - 1) && yA == wallY && yB == wallY - 1 && xB == wx) {
					return true;
				}
			}
		} else if (wallDirection == Direction.FORWARD) {
			for (int wy = wallY; wy < wallY + wallLenght; wy ++) {
				if ((yA == wy && xA == wallX - 1 && xB == wallX && (yB == yA || yB == yA - 1 || yB == yA + 1)) 
						|| (yA == wy + 1) && xA == wallX - 1 && xB == wallX && yB == wy 
						|| (yA == wy - 1) && xA == wallX - 1 && xB == wallX && yB == wy
						|| (yA == wy && xA == wallX && xB == wallX - 1 && (yB == yA || yB == yA - 1 || yB == yA + 1))
						|| (yA == wy + 1) && xA == wallX && xB == wallX - 1 && yB == wy 
						|| (yA == wy - 1) && xA == wallX && xB == wallX - 1 && yB == wy) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "Wall(" + 
				this.getLocation() + ", " + 
				this.getDirection() + ", " + 
				this.getLenght() + ")";
	}
	
}
