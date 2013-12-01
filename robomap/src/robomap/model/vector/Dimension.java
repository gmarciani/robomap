package robomap.model.vector;

import java.io.Serializable;

public class Dimension extends Vector implements Serializable {
	
	private static final long serialVersionUID = -367509300511668919L;
	
	public Dimension(int width, int height) {
		super(width, height);
	}

	public int getWidth() {
		return this.getX();
	}

	public void setWidth(int width) {
		this.setX(width);
	}

	public int getHeight() {
		return this.getY();
	}

	public void setHeight(int height) {
		this.setY(height);
	}
	
	@Override
	public String toString() {
		return "Dimension(" + 
				this.getWidth() + ";" + 
				this.getHeight() + ")";
	}

}
