package robomap.model.vector;

import java.io.Serializable;

/**
 * @project robomap
 *
 * @package robomap.model.vector
 *
 * @class Direction
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public enum Direction implements Serializable {
	
	FORWARD("FORWRAD", new Vector(0, 1), 0),
	BACKWARD("BACKWARD", new Vector(0, -1), 180),
	LEFT("LEFT", new Vector(-1, 0), 90),
	RIGHT("RIGHT", new Vector(1, 0), -90),
	DIAGONAL_TOP_LEFT("DIAGONALTOPLEFT", new Vector(-1, 1), 45),
	DIAGONAL_TOP_RIGHT("DIAGONALTOPRIGHT", new Vector(1, 1), -45),
	DIAGONAL_BOTTOM_LEFT("DIAGONALBOTTOMLEFT", new Vector(-1, -1), 135),
	DIAGONAL_BOTTOM_RIGHT("DIAGONALBOTTOMRIGHT", new Vector(1, -1), -135),
	NONE("None", new Vector(0, 0), 0);
	
	private final String name;
	private final Vector versor;
	private final int rotation;
	
	Direction(final String name, final Vector versor, final int rotation) {
		this.name = name;
		this.versor = versor;
		this.rotation = rotation;
	}

	public String getName() {
		return this.name;
	}
	
	public Vector getVersor() {
		return this.versor;
	}
	
	public int getRotation() {
		return this.rotation;
	}

	public static Direction computeDirection(Location source, Location destination) {
		double sourceX = source.getX();
		double sourceY = source.getY();
		double destinationX = destination.getX();
		double destinationY = destination.getY();
		
		if (destinationX > sourceX) {
			if(destinationY > sourceY) {
				return Direction.DIAGONAL_TOP_RIGHT;
			} else if (destinationY < sourceY) {
				return Direction.DIAGONAL_BOTTOM_RIGHT;
			} else {
				return Direction.RIGHT;
			}
			
		} else if (destinationX < sourceX){
			if(destinationY > sourceY) {
				return Direction.DIAGONAL_TOP_LEFT;
			} else if(destinationY < sourceY) {
				return Direction.DIAGONAL_BOTTOM_LEFT;
			} else {
				return Direction.LEFT;
			}
		} else {
			if (destinationY > sourceY) {
				return Direction.FORWARD;
			} else if (destinationY < sourceY) {
				return Direction.BACKWARD;
			} else {
				return Direction.NONE;
			}
		}
	}

	public static Direction getOpposite(Direction direction) {
		if (direction == Direction.FORWARD) {
			return Direction.BACKWARD;
		} else if (direction == Direction.BACKWARD) {
			return Direction.FORWARD;
		} else if (direction == Direction.RIGHT) {
			return Direction.LEFT;
		} else if (direction == Direction.LEFT) {
			return Direction.RIGHT;
		} else if (direction == Direction.DIAGONAL_TOP_LEFT) {
			return Direction.DIAGONAL_BOTTOM_RIGHT;
		} else if (direction == Direction.DIAGONAL_TOP_RIGHT) {
			return Direction.DIAGONAL_BOTTOM_LEFT;
		} else if (direction == Direction.DIAGONAL_BOTTOM_LEFT) {
			return Direction.DIAGONAL_TOP_RIGHT;
		} else if (direction == Direction.DIAGONAL_BOTTOM_RIGHT) {
			return Direction.DIAGONAL_TOP_LEFT;
		} else {
			return Direction.NONE;
		}
	}

}
