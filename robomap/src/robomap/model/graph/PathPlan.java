package robomap.model.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import robomap.model.base.Direction;
import robomap.model.robot.Movement;

public class PathPlan implements Serializable {
	
	private static final long serialVersionUID = -3311063790419252246L;
	
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
		return "PathPlan(" + this.getMovements().toString() + ")";
	}
	
	

}
