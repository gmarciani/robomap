package robomap.control;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

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
import robomap.model.graph.Path;
import robomap.model.graph.PathPlan;
import robomap.model.home.Home;
import robomap.model.home.Room;
import robomap.model.object.Action;
import robomap.model.object.Object;
import robomap.model.robot.Movement;

public class RobotController {	
	
	private XMLController xmlController;
	private GraphController graphController;
	private DisplayController displayController;
	private GPSController gpsController;
	private MotorController motorController;
	
	private HomeDAO homeDAO;
	private RoomDAO roomDAO;
	private ObjectDAO objectDAO;
	private ActionDAO actionDAO;
	
	private String robotName;
	private Home currentHome;
	private Object payload;
		
	public RobotController(String robotName) {
		this.setRobotName(robotName);
		this.gpsController = new GPSController();
		this.motorController = new MotorController(this.gpsController);
		this.displayController = DisplayController.getInstance();
		this.xmlController = XMLController.getInstance();
		this.graphController = GraphController.getInstance();
		this.homeDAO = HomeJDBCDAO.getInstance();
		this.roomDAO = RoomJDBCDAO.getInstance();
		this.objectDAO = ObjectJDBCDAO.getInstance();
		this.actionDAO = ActionJDBCDAO.getInstance();
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
	
	private void move(Movement movement) {
		this.motorController.move(movement);
		this.displayController.showStatus(this.getRobotName(), this.getCurrentHome(), this.gpsController.getLocation());
	}
	
	public void move(PathPlan pathPlan) {
		Location destination = pathPlan.getDestination();
		while (!this.gpsController.getLocation().equals(destination)) {
			Movement nextMovement = pathPlan.getNextMovement();
			Location nextLocation = Location.computeLocation(this.gpsController.getLocation(), nextMovement);
			if (this.objectDAO.isThereAny(nextLocation)) {
				pathPlan = this.getPathPlanTo(destination);
				continue;
			}
			this.unlockLocation(this.gpsController.getLocation());
			this.lockLocation(nextLocation);
			this.move(nextMovement);
		}
	}
	
	private void lockLocation(Location nextLocation) {
		// TODO Auto-generated method stub
		
	}

	private void unlockLocation(Location location) {
		// TODO Auto-generated method stub
		
	}

	public Home importHomeFromXML(String path) {
		Home home = null;
		try {
			home = this.xmlController.parsePlanimetry(path);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			Log.printXMLException("RobotController", "ImportHomeFromXML", e);
		}
		
		this.homeDAO.saveHome(home);
		
		this.displayController.showImportedHome(this.getRobotName(), home);
		return home;
	}
	
	public void selectHome() {
		List<Home> allHomes = this.homeDAO.getAll();
		int choice = this.displayController.selectHome(this.getRobotName(), allHomes);
		Home selectedHome = allHomes.get(choice);
		this.setCurrentHome(selectedHome);
	}
	
	public Home getCurrentHome() {
		return this.currentHome;
	}

	public void setCurrentHome(Home home) {
		this.currentHome = home;
		Location startLocation = this.getStartLocation();
		this.gpsController.setLocation(startLocation);
		this.displayController.showStatus(this.getRobotName(), this.getCurrentHome(), this.gpsController.getLocation());
	}

	public Location getStartLocation() {
		Home home = this.getCurrentHome();
		Location startLocation = this.homeDAO.getStartLocation(home);
		return startLocation;
	}

	public PathPlan getPathPlanTo(Location destination) {
		Location source = this.gpsController.getLocation();
		Path path = this.graphController.computePath(this.getCurrentHome(), source, destination);
		PathPlan pathPlan = PathPlan.computePathPlan(path);
		return pathPlan;
	}

	public Location getLocation(Room room) {
		return this.roomDAO.getLocation(room);
	}

	public Location getLocation(Object object) {
		return this.objectDAO.getLocation(object);
	}
	
	public Location getLocation(Object object, Action action) {
		return this.objectDAO.getLocation(object, action);
	}

	public Location getLocation(Object object, Direction direction) {
		return this.objectDAO.getLocation(object,  direction);
	}

	public List<String> getAvailableActions(Object object) {
		return this.objectDAO.getActions(object);
	}
	
	public void doAction(Object object, Action action) {
		if (this.gpsController.getLocation().equals(this.objectDAO.getLocation(object, action))) {
			this.objectDAO.changeStatus(object, action.getStatus());
			Log.printAction(object, action);
		}		
	}

	public Action selectAction(Object object) {
		List<Action> actions = this.actionDAO.getActions(object);
		int choice = this.displayController.selectAction(this.getRobotName(), actions);
		Action action = actions.get(choice);
		return action;
	}

	public void addPayload(Object object) {
		this.setPayload(object);		
	}

	public void releasePayload() {
		Object object = this.getPayload();
		Location location = this.gpsController.getLocation();
		this.objectDAO.setLocation(object, location);
		this.setPayload(null);		
	}

}
