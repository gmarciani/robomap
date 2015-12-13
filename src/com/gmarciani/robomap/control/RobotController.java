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
import robomap.exception.NotFoundException;
import robomap.exception.ObjectDimensionException;
import robomap.exception.ObjectNotFoundException;
import robomap.exception.RoomNotFoundException;
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
			startLocation = this.getClosestLocation(startLocation, 3);
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
		Location closestStartLocation = this.getClosestLocation(startLocation, 3);
		return closestStartLocation;
	}
	
	public Location getRoomLocation(String roomName) throws RoomNotFoundException {
		this.displayController.showCommandComputeRoomLocation(this.getRobotName(), this.getCurrentHome().getName(), roomName);
		Room room = this.roomDAO.getRoom(this.getCurrentHome().getName(), roomName);
		if (room == null) throw new RoomNotFoundException(roomName);		
		Location roomLocation = room.getMiddleLocation();
		Location destination = this.getClosestLocation(roomLocation, 3);
		this.displayController.showRoomFound(this.getRobotName(), this.getCurrentHome().getName(), roomName, destination);
		return destination;
	}
	
	public Location getObjectLocation(String objectName) throws ObjectNotFoundException {
		this.displayController.showCommandComputeObjectLocation(this.getRobotName(), this.getCurrentHome().getName(), objectName);
		Object object = this.objectDAO.getObject(this.getCurrentHome().getName(), objectName);
		if (object == null) throw new ObjectNotFoundException(objectName);
		Location destination = this.getClosestLocationToObject(object);
		this.displayController.showObjectFound(this.getRobotName(), this.getCurrentHome().getName(), objectName, destination);
		return destination;
	}	

	public Location getObjectLocation(String roomName, String objectName) throws ObjectNotFoundException {
		this.displayController.showCommandComputeObjectLocation(this.getRobotName(), this.getCurrentHome().getName(), roomName, objectName);
		Object object = this.objectDAO.getObject(this.getCurrentHome().getName(), roomName, objectName);
		if (object == null) throw new ObjectNotFoundException(objectName);
		Location destination = this.getClosestLocationToObject(object);
		this.displayController.showObjectFound(this.getRobotName(), this.getCurrentHome().getName(), objectName, destination);
		return destination;
	}	
	
	public Location getObjectLocation(String objectName, Direction direction) throws ObjectNotFoundException {
		this.displayController.showCommandComputeObjectLocation(this.getRobotName(), this.getCurrentHome().getName(), objectName, direction);
		Object object = this.objectDAO.getObject(this.getCurrentHome().getName(), objectName);
		if (object == null) throw new ObjectNotFoundException(objectName);
		Location objectEdge = object.getObjectEdge(direction);
		Location destination = this.getSelectiveClosestLocation(objectEdge, 3, object.getCoveredLocations());
		this.displayController.showObjectFound(this.getRobotName(), this.getCurrentHome().getName(), objectName, destination);
		return destination;
	}	

	public Location getObjectLocation(String roomName, String objectName, Direction direction) throws ObjectNotFoundException {
		this.displayController.showCommandComputeObjectLocation(this.getRobotName(), this.getCurrentHome().getName(), roomName, objectName, direction);
		Object object = this.objectDAO.getObject(this.getCurrentHome().getName(), roomName, objectName);
		if (object == null) throw new ObjectNotFoundException(objectName);
		Location objectEdge = object.getObjectEdge(direction);
		Location destination = this.getSelectiveClosestLocation(objectEdge, 3, object.getCoveredLocations());
		this.displayController.showObjectFound(this.getRobotName(), this.getCurrentHome().getName(), objectName, destination);
		return destination;
	}	
	
	public Location getObjectLocation(String objectName, Direction direction, String nearObjectName) throws ObjectNotFoundException {
		this.displayController.showCommandComputeObjectNearLocation(this.getRobotName(), this.getCurrentHome().getName(), objectName, direction, nearObjectName);
		Object object = this.objectDAO.getObject(this.getCurrentHome().getName(), objectName, direction, nearObjectName);
		if (object == null) throw new ObjectNotFoundException(objectName);
		Location destination = this.getClosestLocationToObject(object);
		this.displayController.showObjectFound(this.getRobotName(), this.getCurrentHome().getName(), objectName, destination);
		return destination;
	}
	
	public Object getObject(String objectName) {
		Object object = this.objectDAO.getObject(this.getCurrentHome().getName(), objectName);
		return object;
	}

	public Object getObject(String roomName, String objectName) {
		Object object = this.objectDAO.getObject(this.getCurrentHome().getName(), roomName, objectName);
		return object;
	}
	
	public void checkObjectStatus(String objectName) {
		this.displayController.showCommandCheckObjectStatus(this.getRobotName(), this.getCurrentHome().getName(), objectName);
		Object object = this.objectDAO.getObject(this.getCurrentHome().getName(), objectName);
		String status = object.getStatus();
		this.displayController.showObjectStatus(this.getRobotName(), this.getCurrentHome().getName(), objectName, status);
	}
	
	public void makeAction(Object object, Interaction action) {
		this.displayController.showCommandMakeAction(this.getRobotName(), this.getCurrentHome().getName(), action, object.getName());
		this.motorController.doAction(action);
		this.objectDAO.setStatus(this.getCurrentHome().getName(), object.getName(), action.getStatus());
	}	
	
	public Object getPayload() {
		return this.payload;
	}

	private void setPayload(Object payload) {
		this.payload = payload;
	}
	
	public void addPayload(Object object) throws ObjectDimensionException {
		this.displayController.showCommandAddPayload(this.getRobotName(), this.getCurrentHome().getName(), object.getName());
		if (object.getDimension().getWidth() > 1 || object.getDimension().getHeight() > 1) throw new ObjectDimensionException(object.getName());
		this.setPayload(object);		
	}

	public void releasePayload() {
		Object object = this.getPayload();
		Location location = this.gpsController.getLocation();
		this.objectDAO.setLocation(this.getCurrentHome().getName(), object.getName(), location);
		this.displayController.showNewObjectLocation(this.getRobotName(), this.getCurrentHome().getName(), object.getName(), object.getLocation(), location);
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
				Location sblockingLocation = this.getSelectiveClosestLocation(nextLocation, 3, this.gpsController.getLocation());
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

	private Location getClosestLocation(Location location, int maxDistance) {
		if (this.isThereObject(location) || this.isLockedLocation(location)) {
			List<Direction> randomDirections = RandomController.getRandomDirections();
			for (int distance = 1; distance <= maxDistance; distance ++) {
				for (Direction direction : randomDirections) {				
					Location closestLocation = Location.computeLocation(location, Direction.FORWARD, direction, distance);
					if (this.isValidLocation(closestLocation) 
							&& !this.isThereWall(Location.computeLocation(location, Direction.FORWARD, Direction.getOpposite(direction), 1), closestLocation)
							&& !this.isThereObject(closestLocation) 
							&& !this.isLockedLocation(closestLocation)) return closestLocation;
				}
			}				
		}
		return location;
	}	
	
	private Location getSelectiveClosestLocation(Location location, int maxDistance, Location withoutLocation) {
		if (this.isThereObject(location) || this.isLockedLocation(location)) {
			List<Direction> randomDirections = RandomController.getRandomDirections();
			for (int distance = 1; distance <= maxDistance; distance ++) {
				for (Direction direction : randomDirections) {
					Location closestLocation = Location.computeLocation(location, Direction.FORWARD, direction, distance);
					if (closestLocation.equals(withoutLocation)) continue;
					if (this.isValidLocation(closestLocation) 
							&& !this.isThereWall(Location.computeLocation(location, Direction.FORWARD, Direction.getOpposite(direction), 1), closestLocation)
							&& !this.isThereObject(closestLocation) 
							&& !this.isLockedLocation(closestLocation)) return closestLocation;
				}
			}				
		}
		return location;
	}	
	
	private Location getSelectiveClosestLocation(Location location, int maxDistance, List<Location> withoutLocations) {
		if (this.isThereObject(location) || this.isLockedLocation(location)) {
			List<Direction> randomDirections = RandomController.getRandomDirections();
			for (int distance = 1; distance <= maxDistance; distance ++) {
				for (Direction direction : randomDirections) {
					Location closestLocation = Location.computeLocation(location, Direction.FORWARD, direction, distance);
					if (withoutLocations.contains(closestLocation)) continue;
					if (this.isValidLocation(closestLocation) 
							&& !this.isThereWall(Location.computeLocation(location, Direction.FORWARD, Direction.getOpposite(direction), 1), closestLocation)
							&& !this.isThereObject(closestLocation) 
							&& !this.isLockedLocation(closestLocation)) return closestLocation;
				}
			}				
		}
		return location;
	}	
	
	private Location getClosestLocationToObject(Object object) throws ObjectNotFoundException {
		Location location = object.getMiddleLocation();
		int maxDistance = Math.max(this.getCurrentHome().getDimension().getWidth(), this.getCurrentHome().getDimension().getHeight());
		for (int distance = 1; distance < maxDistance; distance ++) {
			for (Direction direction : Direction.values()) {
				location = Location.computeLocation(object.getMiddleLocation(), object.getOrientation(), direction, distance);
				if (this.isValidLocation(location) 
						&& !this.isThereWall(Location.computeLocation(location, object.getOrientation(), Direction.getOpposite(direction), 1), location)
						&& !this.isThereObject(location) 
						&& !this.isLockedLocation(location)) return location;
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

	public void showException(NotFoundException exc) {
		this.displayController.showError(exc.getMessage());
	}

	public void showException(ObjectDimensionException exc) {
		this.displayController.showError(exc.getMessage());
	}

	

}
