package robomap.model.base;

import java.io.Serializable;

public enum Direction implements Serializable {
	
	FORWARD("Forward", 0, 1),
	BACK("Back", 0, -1),
	LEFT("Left", -1, 0),
	RIGHT("Right", 1, 0),
	DIAGONAL_TOP_LEFT("DiagonalTopLeft", -1, 1),
	DIAGONAL_TOP_RIGHT("DiagonalTopRight", 1, 1),
	DIAGONAL_BOTTOM_LEFT("DiagonalBottomLeft", -1, -1),
	DIAGONAL_BOTTOM_RIGHT("DiagonalBottomRight", 1, -1),
	NONE("None", 0, 0);
	
	private final String name;
	private final float versorX;
	private final float versorY;
	
	Direction(final String name, final float versorX, final float versorY) {
		this.name = name;
		this.versorX = versorX;
		this.versorY = versorY;
	}

	public String getName() {
		return this.name;
	}
	
	public float getVersorX() {
		return this.versorX;
	}
	
	public float getVersorY() {
		return this.versorY;
	}

	public static Direction computeDirection(Location source, Location destination) {
		int sourceX = source.getX();
		int sourceY = source.getY();
		int destinationX = destination.getX();
		int destinationY = destination.getY();
		
		if(destinationX > sourceX) {
			if(destinationY > sourceY) {
				return Direction.DIAGONAL_TOP_RIGHT;
			} else if(destinationY < sourceY) {
				return Direction.DIAGONAL_BOTTOM_RIGHT;
			} else {
				return Direction.RIGHT;
			}
			
		} else if(destinationX < sourceX){
			if(destinationY > sourceY) {
				return Direction.DIAGONAL_TOP_LEFT;
			} else if(destinationY < sourceY) {
				return Direction.DIAGONAL_BOTTOM_LEFT;
			} else {
				return Direction.LEFT;
			}
		} else {
			if(destinationY > sourceY) {
				return Direction.FORWARD;
			} else if(destinationY < sourceY) {
				return Direction.BACK;
			} else {
				return Direction.NONE;
			}
		}
	}

}
