package robomap.control;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import robomap.database.HomeDAO;
import robomap.database.LockDAO;
import robomap.database.ObjectDAO;
import robomap.database.RoomDAO;
import robomap.database.impl.HomeJDBCDAO;
import robomap.database.impl.LockJDBCDAO;
import robomap.database.impl.ObjectJDBCDAO;
import robomap.database.impl.RoomJDBCDAO;
import robomap.exception.BlockingLockException;
import robomap.log.Log;
import robomap.model.graph.Path;
import robomap.model.home.Home;
import robomap.model.home.Room;
import robomap.model.home.Wall;
import robomap.model.object.Interaction;
import robomap.model.object.Object;
import robomap.model.robot.MovementPlan;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;
import robomap.model.vector.Movement;

 /**
 * @project robomap
 *
 * @package robomap.control
 *
 * @class RobotController
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class RobotController {	
	
	private DisplayController displayController;
	private GPSController gpsController;
	private MotorController motorController;
	
	private HomeDAO homeDAO;
	private RoomDAO roomDAO;
	private ObjectDAO objectDAO;
	private LockDAO lockDAO;
	
	private String robotName;
	private Home currentHome;
	private Object payload;
	
	private static final long LOCK_WAITING_MILLIS = 2000;
	private static final long WAITING_LOWER_BOUND = 1000;
	private static final long WAITING_UPPER_BOUND = 3000;
		
	public RobotController(String robotName) {
		this.setRobotName(robotName);
		this.gpsController = new GPSController();
		this.motorController = new MotorController(this.gpsController);
		this.displayController = DisplayController.getInstance();
		this.homeDAO = HomeJDBCDAO.getInstance();
		this.roomDAO = RoomJDBCDAO.getInstance();
		this.objectDAO = ObjectJDBCDAO.getInstance();
		this.lockDAO = LockJDBCDAO.getInstance();
		this.removeAllLocks();
	}	

	public String getRobotName() {
		return this.robotName;
	}

	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}
	
	public Home getCurrentHome() {
		return this.currentHome;
	}

	public void setCurrentHome(Home home) {
		this.displayController.showCommandSetHome(this.getRobotName(), home);
		this.currentHome = home;
		Location startLocation = home.getStart();
		try {
			this.lockLocation(startLocation);
		} catch (BlockingLockException exc) {
			if (ControlDebug.D) Log.printException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
			this.displayController.showBlockingLock(this.getRobotName(), this.getCurrentHome().getName(), exc.getLocation());
			startLocation = this.getClosestLocation(startLocation);
		}
		this.gpsController.setLocation(startLocation);		
		this.showStatus();		
	}
	
	public Home importHomeFromXML(String path) {
		this.displayController.showCommandImport(this.getRobotName(), path);
		Home home = null;
		try {
			home = XMLController.parsePlanimetry(path);
		} catch (ParserConfigurationException | SAXException | IOException exc) {
			if (ControlDebug.D) Log.printException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		}
		
		this.homeDAO.saveHome(home);
		
		return home;
	}		
	
	public Location getStartLocation() {
		Location startLocation = this.getCurrentHome().getStart();
		Location closestStartLocation = this.getClosestLocation(startLocation);
		return closestStartLocation;
	}
	
	public Location getRoomLocation(String roomName) {
		Room room = this.roomDAO.getRoom(this.getCurrentHome().getName(), roomName);
		Location roomMiddleLocation = room.getMiddleLocation();
		Location closestRoomMiddleLocation = this.getClosestLocation(roomMiddleLocation);
		return closestRoomMiddleLocation;
	}
	
	public Object getObject(String roomName, String objectName) {
		return this.objectDAO.getObject(this.getCurrentHome().getName(), roomName, objectName);
	}

	public Location getObjectLocation(String roomName, String objectName) {
		return this.objectDAO.getLocation(this.getCurrentHome().getName(), roomName, objectName);
	}
	
	public Location getObjectLocation(String roomName, String objectName, Interaction action) {
		return this.objectDAO.getLocation(this.getCurrentHome().getName(), roomName, objectName, action);
	}

	public Location getObjectLocation(String roomName, String objectName, Direction direction) {
		Object object = this.objectDAO.getObject(this.getCurrentHome().getName(), roomName, objectName);
		Location location = object.getMiddleLocation();
		Location prevLocation = location;
		while (object.comprehend(location)) {
			prevLocation = location;
			location = Location.computeLocation(prevLocation, object.getOrientation(), direction, 1);
		}
		
		if (!this.isValidLocation(location) || this.isThereWall(prevLocation, location) || this.isThereObject(location)) {
			return null;
		}
		return location;
	}	

	public void setObjectOrientation(String roomName, String objectName, Direction orientation) {
		this.objectDAO.setOrientation(this.getCurrentHome().getName(), roomName, objectName, orientation);
	}
	
	public void doAction(Object object, Interaction action) {
		this.motorController.doAction(action);
		this.objectDAO.setStatus(object, action.getStatus());
	}	
	
	public Object getPayload() {
		return this.payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
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
	
	public void shutDown() {
		this.removeAllLocks();
		this.displayController.showCommandShutDown(this.getRobotName());		
	}
	
	public void move(MovementPlan movementPlan) {
		this.displayController.showStatus(this.getRobotName(), this.getCurrentHome(), this.gpsController.getLocation());
		Location destination = movementPlan.getDestination();
		while (!this.gpsController.getLocation().equals(destination)) {
			Movement nextMovement = movementPlan.getNextMovement();
			Location nextLocation = Location.computeLocation(this.gpsController.getLocation(), nextMovement);
			if (this.isThereObject(nextLocation)) {
				movementPlan = this.getSelectiveMovementPlanTo(destination, nextLocation);
				continue;
			}
			try {
				this.lockLocation(nextLocation);
			} catch (BlockingLockException exc) {
				if (ControlDebug.D) Log.printException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
				this.displayController.showBlockingLock(this.getRobotName(), this.getCurrentHome().getName(), exc.getLocation());
				this.displayController.showCommandComputeSblockingLocation(this.getRobotName(), this.getCurrentHome().getName(), exc.getLocation());
				Location sblockingLocation = this.getSelectiveClosestLocation(nextLocation, this.gpsController.getLocation());
				this.displayController.showSblockingLocation(this.getRobotName(), this.getCurrentHome().getName(), sblockingLocation);
				MovementPlan sblockingMovementPlan = this.getMovementPlanTo(sblockingLocation);
				this.move(sblockingMovementPlan);
				this.waitRandom();
				movementPlan = this.getMovementPlanTo(destination);
				continue;
			}
			this.move(nextMovement);
		}
		this.showStatus();
	}	

	private void waitRandom() {
		long time = RandomController.getRandom(WAITING_LOWER_BOUND, WAITING_UPPER_BOUND);
		try {
			Thread.sleep(time);
		} catch (InterruptedException exc) {
			if (ControlDebug.D) Log.printException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		}
	}

	public MovementPlan getMovementPlanTo(Location destination) {
		Location source = this.gpsController.getLocation();
		this.displayController.showCommandComputePathPlan(this.getRobotName(), this.getCurrentHome(), source, destination);
		this.updateObjects();
		Path path = GraphController.computePath(this.getCurrentHome(), source, destination);
		MovementPlan movementPlan = MovementPlan.computeMovementPlan(path);
		return movementPlan;
	}
	
	public MovementPlan getSelectiveMovementPlanTo(Location destination, Location withoutLocation) {
		Location source = this.gpsController.getLocation();
		this.displayController.showCommandComputeSelectivePathPlan(this.getRobotName(), this.getCurrentHome(), source, destination, withoutLocation);
		this.updateObjects();
		Path path = GraphController.computeSelectivePath(this.getCurrentHome(), source, destination, withoutLocation);
		MovementPlan movementPlan = MovementPlan.computeMovementPlan(path);
		return movementPlan;
	}
	
	public MovementPlan getSelectiveMovementPlanTo(Location destination, List<Location> withoutLocations) {
		Location source = this.gpsController.getLocation();
		this.displayController.showCommandComputeSelectivePathPlan(this.getRobotName(), this.getCurrentHome(), source, destination, withoutLocations);
		this.updateObjects();
		Path path = GraphController.computeSelectivePath(this.getCurrentHome(), source, destination, withoutLocations);
		MovementPlan movementPlan = MovementPlan.computeMovementPlan(path);
		return movementPlan;
	}
	
	private void move(Movement movement) {
		Location prevLocation = this.gpsController.getLocation();
		Location nextLocation = Location.computeLocation(prevLocation, movement);
		this.displayController.showCommandMovement(this.getRobotName(), this.getCurrentHome(), prevLocation, nextLocation, movement);
		this.motorController.move(movement);
	}
	
	private void lockLocation(Location location) throws BlockingLockException {
		if (this.isLockedLocation(location)) {
			try {
				Thread.sleep(LOCK_WAITING_MILLIS);
			} catch (InterruptedException exc) {
				if (ControlDebug.D) Log.printException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
			}
			if (this.isLockedLocation(location)) throw new BlockingLockException(location);
		}
		
		this.lockDAO.lock(this.getCurrentHome().getName(), this.getRobotName(), location);
	}

	private boolean isLockedLocation(Location location) {
		return this.lockDAO.isLock(this.getCurrentHome().getName(), this.getRobotName(), location);
	}
	
	private void removeAllLocks() {
		this.lockDAO.deleteLock(this.getRobotName());
	}	

	private Location getClosestLocation(Location location) {
		if (this.isThereObject(location) || this.isLockedLocation(location)) {
			List<Direction> randomDirections = RandomController.getRandomDirections();
			for (Direction direction : randomDirections) {
				Location closestLocation = Location.computeLocation(location, Direction.FORWARD, direction, 1);
				if (this.isValidLocation(closestLocation) 
						&& !this.isThereWall(this.gpsController.getLocation(), closestLocation)
						&& !this.isThereObject(closestLocation) 
						&& !this.isLockedLocation(closestLocation)) return closestLocation;
			}	
		}
		return location;
	}	
	
	private Location getSelectiveClosestLocation(Location location, Location withoutLocation) {
		if (this.isThereObject(location) || this.isLockedLocation(location)) {
			List<Direction> randomDirections = RandomController.getRandomDirections();
			for (Direction direction : randomDirections) {
				Location closestLocation = Location.computeLocation(location, Direction.FORWARD, direction, 1);
				if (closestLocation.equals(withoutLocation)) continue;
				if (this.isValidLocation(closestLocation) 
						&& !this.isThereWall(this.gpsController.getLocation(), closestLocation)
						&& !this.isThereObject(closestLocation) 
						&& !this.isLockedLocation(closestLocation)) return closestLocation;
			}	
		}
		return location;
	}	
	
	private Location getSelectiveClosestLocation(Location location, List<Location> withoutLocations) {
		if (this.isThereObject(location) || this.isLockedLocation(location)) {
			List<Direction> randomDirections = RandomController.getRandomDirections();
			for (Direction direction : randomDirections) {
				Location closestLocation = Location.computeLocation(location, Direction.FORWARD, direction, 1);
				if (withoutLocations.contains(closestLocation)) continue;
				if (this.isValidLocation(closestLocation) 
						&& !this.isThereWall(this.gpsController.getLocation(), closestLocation)
						&& !this.isThereObject(closestLocation) 
						&& !this.isLockedLocation(closestLocation)) return closestLocation;
			}	
		}
		return location;
	}	

	private boolean isValidLocation(Location location) {
		return this.getCurrentHome().comprehend(location);
	}

	private boolean isThereObject(Location location) {
		List<Object> objects = this.objectDAO.getAllObjects(this.getCurrentHome().getName());
		for (Object object : objects) {
			if (object.comprehend(location)) return true;
		}
		return false;
	}

	private boolean isThereWall(Location locationA, Location locationB) {
		List<Wall> walls = this.getCurrentHome().getWalls();
		for (Wall wall : walls) {
			if (wall.blocks(locationA, locationB)) return true;
		}
		return false;
	}	
	
	private void updateObjects() {
		this.getCurrentHome().setObjects(this.objectDAO.getAllObjects(this.getCurrentHome().getName()));
	}	
	
	private void showStatus() {
		this.displayController.showStatus(this.getRobotName(), this.getCurrentHome(), this.gpsController.getLocation());
	}

}
