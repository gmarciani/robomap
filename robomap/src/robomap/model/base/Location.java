package robomap.model.base;

import java.io.Serializable;

public class Location implements Serializable {

	private static final long serialVersionUID = -9033935801147702920L;
	
	private int x;
	private int y;
	
	public Location(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
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
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	@Override
	public String toString() {
		return "Location(" + 
				this.getX() + ";" + 
				this.getY() + ")";
	}

}
