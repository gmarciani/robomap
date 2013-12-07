package robomap.model.robot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import robomap.model.graph.Arc;
import robomap.model.graph.Path;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;
import robomap.model.vector.Movement;

/**
 * @project robomap
 *
 * @package robomap.model.robot
 *
 * @class MovementPlan
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class MovementPlan implements Serializable {
	
	private static final long serialVersionUID = -3311063790419252246L;
	
	private Location source;
	private Location destination;
	private List<Movement> movements = new ArrayList<Movement>();
	private int nextMovement = 0;
	
	public MovementPlan(Location source, Location destination, List<Movement> movements) {
		this.setSource(source);
		this.setDestination(destination);
		this.setMovements(movements);
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
		return this.source;
	}

	public void setSource(Location source) {
		this.source = source;
	}

	public Location getDestination() {
		return this.destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}
	
	public Movement getNextMovement() {
		Movement movement = this.getMovements().get(nextMovement);
		nextMovement ++;
		return movement;
	}	
	
	@Override
	public String toString() {
		return "MovementPlan(" + this.getMovements().toString() + ")";
	}

	public static MovementPlan computeMovementPlan(Path path) {
		List<Arc> arcs = path.getArcs();
		List<Movement> movements = new ArrayList<Movement>();
		Location source = arcs.get(0).getSource().getLocation();
		Location destination = arcs.get(arcs.size() - 1).getDestination().getLocation();
		for (Arc arc : arcs) {
			Movement movement = Movement.computeMovement(arc);
			movements.add(movement);
		}
		return new MovementPlan(source, destination, movements);
	}	

}
