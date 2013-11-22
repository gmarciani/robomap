package robomap.model;

import java.util.ArrayList;
import java.util.List;

public class PathPlan {
	
	List<Movement> movements;
	
	public PathPlan() {
		this.movements = new ArrayList<Movement>();
	}
	
	public List<Movement> getMovements() {
		return this.movements;
	}
	
	public void setMovements(List<Movement> movements) {
		this.movements = movements;
	}
	
	public void addMovement(Movement movement) {
		this.movements.add(movement);
	}
	
	public void addMovement(Direction direction, int module) {
		Movement movement = new Movement(direction, module);
		this.movements.add(movement);
	}
	
	@Override
	public String toString() {
		return this.getMovements().toString();
	}
	
	

}
