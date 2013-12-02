package robomap.control;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import robomap.database.ActionDAO;
import robomap.database.HomeDAO;
import robomap.database.LocationDAO;
import robomap.database.ObjectDAO;
import robomap.database.RoomDAO;
import robomap.database.WallDAO;
import robomap.database.impl.ActionJDBCDAO;
import robomap.database.impl.HomeJDBCDAO;
import robomap.database.impl.LocationJDBCDAO;
import robomap.database.impl.ObjectJDBCDAO;
import robomap.database.impl.RoomJDBCDAO;
import robomap.database.impl.WallJDBCDAO;
import robomap.log.Log;
import robomap.model.graph.Path;
import robomap.model.home.Home;
import robomap.model.object.Action;
import robomap.model.object.Object;
import robomap.model.robot.MovementPlan;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;
import robomap.model.vector.Movement;

public class RobotController {	
	
	private XMLController xmlController;
	private GraphController graphController;
	private DisplayController displayController;
	private GPSController gpsController;
	private MotorController motorController;
	
	private HomeDAO homeDAO;
	private RoomDAO roomDAO;
	private WallDAO wallDAO;
	private ObjectDAO objectDAO;
	private ActionDAO actionDAO;
	private LocationDAO locationDAO;
	
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
		this.wallDAO = WallJDBCDAO.getInstance();
		this.objectDAO = ObjectJDBCDAO.getInstance();
		this.actionDAO = ActionJDBCDAO.getInstance();
		this.locationDAO = LocationJDBCDAO.getInstance();
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
		Location prevLocation = this.gpsController.getLocation();
		Location nextLocation = Location.computeLocation(prevLocation, movement);
		this.displayController.showCommandMovement(this.getRobotName(), this.getCurrentHome(), prevLocation, nextLocation, movement);
		this.motorController.move(movement);
	}
	
	public void move(MovementPlan movementPlan) {
		this.displayController.showStatus(this.getRobotName(), this.getCurrentHome(), this.gpsController.getLocation());
		Location destination = movementPlan.getDestination();
		while (!this.gpsController.getLocation().equals(destination)) {
			Movement nextMovement = movementPlan.getNextMovement();
			Location nextLocation = Location.computeLocation(this.gpsController.getLocation(), nextMovement);
			if (this.objectDAO.isThereAny(nextLocation)) {
				movementPlan = this.getMovementPlanTo(destination);
				continue;
			}
			this.unlockLocation(this.gpsController.getLocation());
			this.lockLocation(nextLocation);
			this.move(nextMovement);
		}
		this.displayController.showStatus(this.getRobotName(), this.getCurrentHome(), this.gpsController.getLocation());
	}
	
	private void lockLocation(Location location) {
		while(this.locationDAO.isLocked(this.getCurrentHome(), location)) {}
		this.locationDAO.lock(this.getCurrentHome(), location);
		
	}

	private void unlockLocation(Location location) {
		this.locationDAO.unlock(this.getCurrentHome(), location);
	}

	public Home importHomeFromXML(String path) {
		this.displayController.showCommandImport(this.getRobotName(), path);
		Home home = null;
		try {
			home = this.xmlController.parsePlanimetry(path);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			Log.printXMLException("RobotController", "ImportHomeFromXML", e);
		}
		
		//this.homeDAO.saveHome(home);
		return home;
	}
	
	public void selectHome() {
		List<Home> allHomes = this.homeDAO.getAll();
		int choice = this.displayController.showSelectHome(this.getRobotName(), allHomes);
		Home selectedHome = allHomes.get(choice);
		this.setCurrentHome(selectedHome);
	}
	
	public Home getCurrentHome() {
		return this.currentHome;
	}

	public void setCurrentHome(Home home) {
		this.displayController.showCommandSetHome(this.getRobotName(), home);
		this.currentHome = home;
		this.gpsController.setLocation(home.getStart());
		this.displayController.showStatus(this.getRobotName(), this.getCurrentHome(), this.gpsController.getLocation());
	}

	public Location getStartLocation() {
		Home home = this.getCurrentHome();
		Location startLocation = home.getStart();
		return startLocation;
	}

	public MovementPlan getMovementPlanTo(Location destination) {
		Location source = this.gpsController.getLocation();
		this.displayController.showCommadComputePathPlan(this.getRobotName(), this.getCurrentHome(), source, destination);
		Path path = this.graphController.computePath(this.getCurrentHome(), source, destination);
		MovementPlan movementPlan = MovementPlan.computeMovementPlan(path);
		return movementPlan;
	}

	public Location getRoomLocation(String roomName) {
		return this.roomDAO.getLocation(this.getCurrentHome().getName(), roomName);
	}
	
	public Object getObject(String roomName, String objectName) {
		return this.objectDAO.getObject(this.getCurrentHome().getName(), roomName, objectName);
	}

	public Location getObjectLocation(String roomName, String objectName) {
		return this.objectDAO.getLocation(this.getCurrentHome().getName(), roomName, objectName);
	}
	
	public Location getObjectLocation(String roomName, String objectName, Action action) {
		return this.objectDAO.getLocation(this.getCurrentHome().getName(), roomName, objectName, action);
	}

	public Location getObjectLocation(String roomName, String objectName, Direction direction) {
		Object object = this.objectDAO.getObject(this.getCurrentHome().getName(), roomName, objectName);
		Location location = object.getMiddleLocation();
		Location prevLocation = location;
		while (object.comprehend(location)) {
			prevLocation = location;
			location = Location.getRelativeLocation(location, object.getOrientation(), direction);
		}
		
		if (!this.isValidLocation(location) || this.isThereWall(prevLocation, location) || this.isThereObject(location)) {
			return null;
		}
		return location;
	}	

	public void setObjectOrientation(String roomName, String objectName, Direction orientation) {
		this.objectDAO.setOrientation(this.getCurrentHome().getName(), roomName, objectName, orientation);
	}

	private boolean isValidLocation(Location location) {
		return this.getCurrentHome().comprehend(location);
	}

	private boolean isThereObject(Location location) {
		return this.objectDAO.isThereAny(location);
	}

	private boolean isThereWall(Location locationA, Location locationB) {
		return this.wallDAO.isThereAny(locationA, locationB);
	}

	public List<String> getAvailableActions(Object object) {
		return this.objectDAO.getActions(object);
	}
	
	public Action selectAction(Object object) {
		List<Action> actions = this.actionDAO.getActions(object);
		int choice = this.displayController.showSelectAction(this.getRobotName(), actions);
		Action action = actions.get(choice);
		return action;
	}
	
	public void doAction(Object object, Action action) {
		if (this.gpsController.getLocation().equals(this.objectDAO.getLocation(object, action))) {
			this.motorController.doAction(action);
			this.objectDAO.setStatus(object, action.getStatus());
		}		
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

	public void printStatus() {
		this.displayController.showStatus(this.getRobotName(), this.getCurrentHome(), this.gpsController.getLocation());
	}

}
