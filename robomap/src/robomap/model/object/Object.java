package robomap.model.object;

import java.io.Serializable;

import robomap.model.base.Dimension;
import robomap.model.base.Location;

public class Object implements Serializable {
	
	private static final long serialVersionUID = -8604922613106336953L;
	
	private String name;
	private Dimension dimension;
	private Location location;
	private String status;
	
	public Object(String name, Dimension dimension, Location location, String status) {
		this.setName(name);
		this.setDimension(dimension);
		this.setLocation(location);
		this.setStatus(status);
	}
	
	public Object(String name, Dimension dimension, Location location) {
		this.setName(name);
		this.setDimension(dimension);
		this.setLocation(location);
	}
	
	public Object(String name, Dimension dimension) {
		this.setName(name);
		this.setDimension(dimension);
		this.setLocation(location);
	}
	
	public Object() {
		
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
	

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Object(" + 
				this.getName() + ";" + 
				this.getDimension().toString() + ";" + 
				this.getLocation().toString() + ";" + 
				this.getStatus() + ")";
	}

}
