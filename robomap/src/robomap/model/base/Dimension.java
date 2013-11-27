package robomap.model.base;

import java.io.Serializable;

public class Dimension implements Serializable {
	
	private static final long serialVersionUID = -367509300511668919L;
	
	private int width;
	private int height;
	
	public Dimension(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	@Override
	public String toString() {
		return "Dimension(" + 
				this.getWidth() + ";" + 
				this.getHeight() + ")";
	}

}
