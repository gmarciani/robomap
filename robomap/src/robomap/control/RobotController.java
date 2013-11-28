package robomap.control;

import java.io.IOException;
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
	
	private static XMLController xmlController;
	private static GraphController graphController;
	private static DisplayController displayController;
	
	private static HomeDAO homeDAO;
	private static RoomDAO roomDAO;
	private static ObjectDAO objectDAO;
	private static ActionDAO actionDAO;
	
	private String robotName;
	private Home currentHome;
	private Location currentLocation;
	private Object payload;
	private DirectedGraph<Node, Arc> graph;
		
	public RobotController(String robotName) {
		this.setRobotName(robotName);
		displayController = DisplayController.getInstance();
		xmlController = XMLController.getInstance();
		graphController = GraphController.getInstance();
		homeDAO = HomeJDBCDAO.getInstance();
		roomDAO = RoomJDBCDAO.getInstance();
		objectDAO = ObjectJDBCDAO.getInstance();
		actionDAO = ActionJDBCDAO.getInstance();
	}
	
	public String getRobotName() {
		return this.robotName;
	}

	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}
	
	public Object getPayload() {
		return this.payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
	public void move(Movement movement) {
		Location newLocation = Location.computeLocation(this.getCurrentLocation(), movement);
		this.setCurrentLocation(newLocation);
		Log.printMovement(movement);
	}
	
	public void move(PathPlan pathPlan) {
		for (Movement movement : pathPlan.getMovements()) {
			this.refreshGraph();
		}
	}
	
	public Home importHomeFromXML(String path) {
		Home home = null;
		try {
			home = xmlController.parsePlanimetry(path);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			Log.printXMLException("RobotController", "ImportHomeFromXML", e);
		}
		
		homeDAO.saveHome(home);
		
		displayController.showImportedHome(this.getRobotName(), home);
		return home;
	}
	
	public void selectHome() {
		List<Home> allHomes = homeDAO.getAll();
		int choice = displayController.selectHome(this.getRobotName(), allHomes);
		Home selectedHome = allHomes.get(choice);
		this.setCurrentHome(selectedHome);
	}
	
	public Home getCurrentHome() {
		return this.currentHome;
	}

	public void setCurrentHome(Home home) {
		this.currentHome = home;
		this.setGraph(graphController.parseGraph(home));
		Location startLocation = this.getStartLocation();
		this.setCurrentLocation(startLocation);
		displayController.showCurrentStatus(this.getRobotName(), this.getCurrentHome(), this.getCurrentLocation());
	}
	
	public Location getCurrentLocation() {
		return this.currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
		if (this.getPayload() != null) objectDAO.setLocation(this.getPayload(), currentLocation);
	}	
	
	public DirectedGraph<Node, Arc> getGraph() {
		return this.graph;
	}

	public void setGraph(DirectedGraph<Node, Arc> graph) {
		this.graph = graph;
	}
	
	private void refreshGraph() {
		Home home = this.getCurrentHome();
		DirectedGraph<Node, Arc> graph = graphController.parseGraph(home);
		this.setGraph(graph);
	}

	public Location getStartLocation() {
		Home home = this.getCurrentHome();
		Location startLocation = homeDAO.getStartLocation(home);
		return startLocation;
	}

	public PathPlan getPathPlanTo(Location destination) {
		Location source = this.getCurrentLocation();
		this.refreshGraph();
		Path path = graphController.computePath(this.getGraph(), source, destination);
		PathPlan pathPlan = PathPlan.computePathPlan(path);
		return pathPlan;
	}

	public Location getLocation(Room room) {
		return roomDAO.getLocation(room);
	}

	public Location getLocation(Object object) {
		return objectDAO.getLocation(object);
	}
	
	public Location getLocation(Object object, Action action) {
		return objectDAO.getLocation(object, action);
	}

	public Location getLocation(Object object, Direction direction) {
		return objectDAO.getLocation(object,  direction);
	}

	public List<String> getAvailableActions(Object object) {
		return objectDAO.getActions(object);
	}
	
	public void doAction(Object object, Action action) {
		if (this.getCurrentLocation().equals(objectDAO.getLocation(object, action))) {
			objectDAO.changeStatus(object, action.getStatus());
			Log.printAction(object, action);
		}		
	}

	public Action selectAction(Object object) {
		List<Action> actions = actionDAO.getActions(object);
		int choice = displayController.selectAction(this.getRobotName(), actions);
		Action action = actions.get(choice);
		return action;
	}

	public void addPayload(Object object) {
		this.setPayload(object);		
	}

	public void releasePayload() {
		Object object = this.getPayload();
		Location location = this.getCurrentLocation();
		objectDAO.setLocation(object, location);
		this.setPayload(null);		
	}

}
