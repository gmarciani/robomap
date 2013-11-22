package robomap.model;

public enum Direction {
	
	FORWARD("Forward"),
	BACK("Back"),
	LEFT("Left"),
	RIGHT("Right"),
	DIAGONAL_TOP_LEFT("DiagonalTopLeft"),
	DIAGONAL_TOP_RIGHT("DiagonalTopRight"),
	DIAGONAL_BOTTOM_LEFT("DiagonalBottomLeft"),
	DIAGONAL_BOTTOM_RIGHT("DiagonalBottomRight"),
	NONE("None");
	
	private final String name;
	
	Direction(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
