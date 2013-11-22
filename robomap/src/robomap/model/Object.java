package robomap.model;

public class Object {
	
	private String name;
	private Dimension dimension;
	private Location location;
	
	public Object(String name, Dimension dimension, Location location) {
		this.setName(name);
		this.setDimension(dimension);
		this.setLocation(location);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Dimension getDimension() {
		return this.dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
