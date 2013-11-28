package robomap.model.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import robomap.model.base.Direction;
import robomap.model.base.Location;
import robomap.model.robot.Movement;

public class PathPlan implements Serializable {
	
	private static final long serialVersionUID = -3311063790419252246L;
	
	private Location source;
	private Location destination;
	private List<Movement> movements = new ArrayList<Movement>();;
	
	public PathPlan(Location source, Location destination, List<Movement> movements) {
		this.setSource(source);
		this.setDestination(destination);
		this.setMovements(movements);
	}
	
	public PathPlan() {
		
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

	public Location getSource() {
		return source;
	}

	public void setSource(Location source) {
		this.source = source;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}
	
	@Override
	public String toString() {
		return "PathPlan(" + this.getMovements().toString() + ")";
	}

	public static PathPlan computePathPlan(Path path) {
		List<Arc> arcs = path.getArcs();
		List<Movement> movements = new ArrayList<Movement>();
		Location source = arcs.get(0).getSource().getLocation();
		Location destination = arcs.get(arcs.size() - 1).getDestination().getLocation();
		for (Arc arc : arcs) {
			Movement movement = Movement.computeMovement(arc);
			movements.add(movement);
		}
		return new PathPlan(source, destination, movements);
	}	

}
