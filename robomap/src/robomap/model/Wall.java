package robomap.model;

public class Wall {
	
	private Location location;
	private Dimension dimension;
	
	public Wall(Location location, Dimension dimension) {
		this.setLocation(location);
		this.setDimension(dimension);
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Dimension getDimension() {
		return this.dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

}
