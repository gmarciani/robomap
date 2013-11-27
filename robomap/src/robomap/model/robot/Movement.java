package robomap.model.robot;

import java.io.Serializable;

import robomap.model.base.Direction;

public class Movement implements Serializable {

	private static final long serialVersionUID = -5995788339352550426L;
	
	private Direction direction;
	private float module;
	
	public Movement(Direction direction, float module) {
		this.setDirection(direction);
		this.setModule(module);
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public float getModule() {
		return this.module;
	}

	public void setModule(float weight) {
		this.module = weight;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		 
        Movement movement = (Movement) o;
 
        return this.getDirection().equals(movement.getDirection()) && this.getModule() == movement.getModule();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + Float.floatToIntBits(module);
		return result;
	}
	
	@Override
	public String toString() {
		return "Movement(" + 
				this.getDirection().getName() + " " +
				this.getModule() + ")";
	}

}
