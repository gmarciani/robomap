package robomap.control;

import robomap.database.HomeDAO;
import robomap.database.ObjectDAO;
import robomap.database.RoomDAO;
import robomap.database.SettingDAO;
import robomap.database.impl.HomeJDBCDAO;
import robomap.database.impl.ObjectJDBCDAO;
import robomap.database.impl.SettingJDBCDAO;
import robomap.database.impl.RoomJDBCDAO;
import robomap.model.Direction;
import robomap.model.Home;
import robomap.model.Location;
import robomap.model.Movement;
import robomap.model.Object;
import robomap.model.PathPlan;
import robomap.model.Room;
import robomap.model.graph.Arc;
import robomap.model.graph.Path;

public class RobotController {
	
	private static RobotController robotController = null;
	private static GraphController graphController = null;
	
	private static HomeDAO homeDAO = null;
	private static RoomDAO roomDAO = null;
	private static ObjectDAO objectDAO = null;
	private static SettingDAO settingDAO = null;
		
	private RobotController() {
		graphController = GraphController.getInstance();
		homeDAO = HomeJDBCDAO.getInstance();
		roomDAO = RoomJDBCDAO.getInstance();
		objectDAO = ObjectJDBCDAO.getInstance();
		settingDAO = SettingJDBCDAO.getInstance();
	}
	
	public static RobotController getInstance() {
		if(robotController == null) {
			robotController = new RobotController();
		}
		return robotController;
	}
	
	public void loadHome(String homeName) {
		Home home = homeDAO.findHome(homeName);
		graphController.parseGraph(home);
		
	}

	public Location getStartLocation() {
		Location startLocation = settingDAO.getStartLocation();
		return startLocation;
	}

	public PathPlan getPathPlan(Location source, Location destination) {
		Path path = graphController.computePath(source, destination);
		PathPlan pathPlan = new PathPlan();
		for (Arc arc : path.getArcs()) {
			Movement movement = this.getMovementFromArc(arc);
			pathPlan.addMovement(movement);
		}
		return pathPlan;
	}

	private Movement getMovementFromArc(Arc arc) {
		Location source = arc.getSource().getLocation();
		Location destination = arc.getDestination().getLocation();
		int weight = arc.getWeight();
		int sourceX = source.getX();
		int sourceY = source.getY();
		int destinationX = destination.getX();
		int destinationY = destination.getY();
		Movement movement = new Movement();
		movement.setModule(weight);
		if(destinationX > sourceX) {
			if(destinationY > sourceY) {
				movement.setDirection(Direction.DIAGONAL_TOP_RIGHT);
			} else if(destinationY < sourceY) {
				movement.setDirection(Direction.DIAGONAL_BOTTOM_RIGHT);
			} else {
				movement.setDirection(Direction.RIGHT);
			}
			
		} else if(destinationX < sourceX){
			if(destinationY > sourceY) {
				movement.setDirection(Direction.DIAGONAL_TOP_LEFT);
			} else if(destinationY < sourceY) {
				movement.setDirection(Direction.DIAGONAL_BOTTOM_LEFT);
			} else {
				movement.setDirection(Direction.LEFT);
			}
		} else {
			if(destinationY > sourceY) {
				movement.setDirection(Direction.FORWARD);
			} else if(destinationY < sourceY) {
				movement.setDirection(Direction.BACK);
			} else {
				movement.setDirection(Direction.NONE);
			}
		}
		return movement;
	}

	public Location getLocation(Room room) {
		Location roomLocation = roomDAO.findLocation(room);
		return roomLocation;
	}

	public Location getLocation(Object object) {
		Location objectLocation = objectDAO.findLocation(object);
		return objectLocation;
	}

}
