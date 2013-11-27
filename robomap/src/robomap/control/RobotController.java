package robomap.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.uci.ics.jung.graph.DirectedGraph;
import robomap.database.ActionDAO;
import robomap.database.HomeDAO;
import robomap.database.ObjectDAO;
import robomap.database.RoomDAO;
import robomap.database.impl.ActionJDBCDAO;
import robomap.database.impl.HomeJDBCDAO;
import robomap.database.impl.ObjectJDBCDAO;
import robomap.database.impl.RoomJDBCDAO;
import robomap.log.Log;
import robomap.model.base.Direction;
import robomap.model.base.Location;
import robomap.model.graph.Arc;
import robomap.model.graph.Node;
import robomap.model.graph.Path;
import robomap.model.graph.PathPlan;
import robomap.model.home.Home;
import robomap.model.home.Room;
import robomap.model.object.Action;
import robomap.model.object.Object;
import robomap.model.robot.Movement;

public class RobotController {
	
	private static RobotController robotController;
	private static DisplayController displayController;
	private static XMLController xmlController;
	private static GraphController graphController;
	
	private static HomeDAO homeDAO;
	private static RoomDAO roomDAO;
	private static ObjectDAO objectDAO;
	private static ActionDAO actionDAO;
	
	private Home currentHome;
	private Location currentLocation;
	private DirectedGraph<Node, Arc> graph;
		
	private RobotController() {
		displayController = DisplayController.getInstance();
		xmlController = XMLController.getInstance();
		graphController = GraphController.getInstance();
		homeDAO = HomeJDBCDAO.getInstance();
		roomDAO = RoomJDBCDAO.getInstance();
		objectDAO = ObjectJDBCDAO.getInstance();
		actionDAO = ActionJDBCDAO.getInstance();
	}
	
	public static RobotController getInstance() {
		if(robotController == null) {
			robotController = new RobotController();
		}
		return robotController;
	}
	
	public void move(Movement movement) {
		int currX = this.getCurrentLocation().getX();
		int currY = this.getCurrentLocation().getY();
		int moveX = (movement.getDirection() == Direction.RIGHT) ? 1 : -1;
		int moveY = (movement.getDirection() == Direction.FORWARD) ? 1 : -1;
		Location newLocation = new Location(currX + moveX, currY + moveY);
		this.setCurrentLocation(newLocation);
		Log.printMovement(movement);
	}
	
	public List<Home> importHomeFromXML(String path) {
		List<Home> listHomes = new ArrayList<Home>();
		try {
			listHomes = xmlController.parsePlanimetry(path);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			Log.printXMLException("RobotController", "ImportHomeFromXML", e);
		}
		/*
		for (Home home : listHomes) {
			homeDAO.saveHome(home);
		}*/
		displayController.showImportedHomes(listHomes);
		return listHomes;
	}
	
	public Home selectHome() {
		List<Home> allHomes = homeDAO.getAll();
		int choice = displayController.selectHome(allHomes);
		Home selectedHome = allHomes.get(choice);
		this.setCurrentHome(selectedHome);
		return selectedHome;
	}
	
	public Home getCurrentHome() {
		return this.currentHome;
	}

	public void setCurrentHome(Home home) {
		this.currentHome = home;
		this.setGraph(graphController.parseGraph(home));
		Location startLocation = this.getStartLocation();
		this.setCurrentLocation(startLocation);
	}
	
	public Location getCurrentLocation() {
		return this.currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}	
	
	public DirectedGraph<Node, Arc> getGraph() {
		return this.graph;
	}

	public void setGraph(DirectedGraph<Node, Arc> graph) {
		this.graph = graph;
	}

	public Location getStartLocation() {
		Home home = this.getCurrentHome();
		Location startLocation = homeDAO.getStartLocation(home);
		return startLocation;
	}

	public PathPlan getPathPlanTo(Location destination) {
		Location source = this.getCurrentLocation();
		Path path = graphController.computePath(this.getGraph(), source, destination);
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
		float module = arc.getWeight();
		Direction direction;
		int sourceX = source.getX();
		int sourceY = source.getY();
		int destinationX = destination.getX();
		int destinationY = destination.getY();
		if(destinationX > sourceX) {
			if(destinationY > sourceY) {
				direction = Direction.DIAGONAL_TOP_RIGHT;
			} else if(destinationY < sourceY) {
				direction = Direction.DIAGONAL_BOTTOM_RIGHT;
			} else {
				direction = Direction.RIGHT;
			}
			
		} else if(destinationX < sourceX){
			if(destinationY > sourceY) {
				direction = Direction.DIAGONAL_TOP_LEFT;
			} else if(destinationY < sourceY) {
				direction = Direction.DIAGONAL_BOTTOM_LEFT;
			} else {
				direction = Direction.LEFT;
			}
		} else {
			if(destinationY > sourceY) {
				direction = Direction.FORWARD;
			} else if(destinationY < sourceY) {
				direction = Direction.BACK;
			} else {
				direction = Direction.NONE;
			}
		}
		return new Movement(direction, module);
	}

	public Location getLocation(Room room) {
		return roomDAO.getLocation(room);
	}

	public Location getLocation(Object object) {
		return objectDAO.getLocation(object);
	}
	
	public Location getLocation(Object object, String actionName) {
		return objectDAO.getLocation(object, actionName);
	}

	public Location getLocation(Object object, Direction direction) {
		return objectDAO.getLocation(object,  direction);
	}

	public List<String> getAvailableActions(Object object) {
		return objectDAO.getActions(object);
	}
	
	public void doAction(Object object, String actionName) {
		if (this.checkActionAvailability(object, actionName) && 
				this.getCurrentLocation().equals(objectDAO.getLocationForAction(object, actionName))) {
			Action action = actionDAO.getAction(actionName);
			objectDAO.changeStatus(object, action.getStatus());
			Log.printAction(object, action);
		}		
	}
	
	private boolean checkActionAvailability(Object object, String actionName) {
		Action action = actionDAO.getAction(actionName);
		return objectDAO.checkActionAvailability(object, action);
	}	
	
	public int getUserChoice(List<String> choices) {
		int choice = displayController.choose(choices);
		return choice;
	}

}
