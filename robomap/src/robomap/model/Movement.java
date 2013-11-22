package robomap.model;

public class Movement {
	
	private Direction direction;
	private int module;
	
	public Movement(Direction direction, int module) {
		this.setDirection(direction);
		this.setModule(module);
	}

	public Movement() {
		
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getModule() {
		return this.module;
	}

	public void setModule(int module) {
		this.module = module;
	}

}
