package robomap.model.robot;


import java.util.ArrayList;
import java.util.List;

import robomap.control.RobotController;
import robomap.exception.ObjectDimensionException;
import robomap.exception.ObjectNotFoundException;
import robomap.exception.RoomNotFoundException;
import robomap.model.home.Home;
import robomap.model.object.Interaction;
import robomap.model.object.Object;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.model.robot
 *
 * @class Robot
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class Robot extends Thread {	
	
	private RobotController robotController;
	private RobotStatus status;
	private List<RobotCommand> commands;
	
	private boolean isActive = false;
	
	public Robot(String robotName) {
		super(robotName);
		this.setStatus(RobotStatus.INITIALIZATION);
		this.initialize(robotName);
		this.setStatus(RobotStatus.ACTIVE);
	}
	
	private void initialize(String robotName) {
		this.robotController = new RobotController(robotName);
		this.setCommands(new ArrayList<RobotCommand>());
		this.isActive = true;
	}
	
	private RobotStatus getStatus() {
		return this.status;
	}

	private void setStatus(RobotStatus status) {
		this.status = status;
	}
	
	private List<RobotCommand> getCommands() {
		return this.commands;
	}

	private void setCommands(List<RobotCommand> commands) {
		this.commands = commands;
	}
	
	private boolean isActive() {
		return this.isActive;
	}

	@Override
	public void run() {
		RobotStatus status = this.getStatus();
		while(this.isActive()) {
			if (status != RobotStatus.EXECUTION 
					&& !this.getCommands().isEmpty()) {
				RobotCommand command = this.getNextCommand();
				this.executeCommand(command);
			}
			status = this.getStatus();
		}	
	}	

	private RobotCommand getNextCommand() {
		RobotCommand command = this.getCommands().get(0);
		this.getCommands().remove(0);
		return command;
	}

	private void executeCommand(RobotCommand command) {
			this.setStatus(RobotStatus.EXECUTION);
			Location location = null;
			MovementPlan movementPlan = null;
			Object object = null;
			switch(command.getOpcode()) {
			case IMPORT:
				Home home = this.robotController.importHomeFromXML(command.getFilePath());
				this.robotController.setCurrentHome(home);
				break;
			case GOTO_START:
				location = this.robotController.getStartLocation();
				movementPlan = this.robotController.getMovementPlanTo(location);
				this.robotController.move(movementPlan);
				break;
			case GOTO_LOCATION:
				movementPlan = this.robotController.getMovementPlanTo(command.getLocation());
				this.robotController.move(movementPlan);
				break;
			case GOTO_ROOM:
				try {
					location = this.robotController.getRoomLocation(command.getRoomName());
					movementPlan = this.robotController.getMovementPlanTo(location);
					this.robotController.move(movementPlan);
				} catch (RoomNotFoundException exc) {
					this.robotController.showException(exc);
				}				
				break;
			case GOTO_OBJECT:
				try {
					location = this.robotController.getObjectLocation(command.getObjectName());
					movementPlan = this.robotController.getMovementPlanTo(location);
					this.robotController.move(movementPlan);
				} catch (ObjectNotFoundException exc) {
					this.robotController.showException(exc);
				}				
				break;
			case GOTO_OBJECT_IN_ROOM:
				try {
					location = this.robotController.getObjectLocation(command.getRoomName(), command.getObjectName());
					movementPlan = this.robotController.getMovementPlanTo(location);
					this.robotController.move(movementPlan);
				} catch (ObjectNotFoundException exc) {
					this.robotController.showException(exc);
				}				
				break;
			case GOTO_OBJECT_WITH_DIRECTION:
				try {
					location = this.robotController.getObjectLocation(command.getObjectName(), command.getDirection());
					movementPlan = this.robotController.getMovementPlanTo(location);
					this.robotController.move(movementPlan);
				} catch (ObjectNotFoundException exc) {
					this.robotController.showException(exc);
				}				
				break;
			case GOTO_OBJECT_IN_ROOM_WITH_DIRECTION:
				try {
					location = this.robotController.getObjectLocation(command.getRoomName(), command.getObjectName(), command.getDirection());
					movementPlan = this.robotController.getMovementPlanTo(location);
					this.robotController.move(movementPlan);
				} catch (ObjectNotFoundException exc) {
					this.robotController.showException(exc);
				}				
				break;
			case GOTO_OBJECT_NEAR_OBJECT:
				try {
					location = this.robotController.getObjectLocation(command.getObjectName(), command.getDirection(), command.getNearObjectName());
				} catch (ObjectNotFoundException exc) {
					this.robotController.showException(exc);
				}
				movementPlan = this.robotController.getMovementPlanTo(location);
				this.robotController.move(movementPlan);
				break;
			case MOVE_OBJECT_TO_ROOM:
				object = this.robotController.getObject(command.getObjectName());
				location = Location.computeLocation(object.getLocation(), object.getOrientation(), Direction.FORWARD, 1);
				movementPlan = this.robotController.getMovementPlanTo(location);
				this.robotController.move(movementPlan);
				try {
					this.robotController.addPayload(object);
					location = this.robotController.getRoomLocation(command.getRoomName());
				} catch (ObjectDimensionException | RoomNotFoundException exc) {
					this.robotController.showException(exc);
				}				
				movementPlan = this.robotController.getMovementPlanTo(location);
				this.robotController.move(movementPlan);
				this.robotController.releasePayload();
				break;
			case MAKE_ACTION_ON_OBJECT:
				object = this.robotController.getObject(command.getObjectName());
				location = Location.computeLocation(object.getLocation(), object.getOrientation(), Direction.FORWARD, 1);
				movementPlan = this.robotController.getMovementPlanTo(location);
				this.robotController.move(movementPlan);		
				this.robotController.makeAction(object, command.getAction());
				break;
			case MAKE_ACTION_ON_OBJECT_IN_ROOM:
				object = this.robotController.getObject(command.getRoomName(), command.getObjectName());
				location = Location.computeLocation(object.getLocation(), object.getOrientation(), Direction.FORWARD, 1);
				movementPlan = this.robotController.getMovementPlanTo(location);
				this.robotController.move(movementPlan);		
				this.robotController.makeAction(object, command.getAction());
				break;
			case CHECK_OBJECT_STATUS:
				try {
					location = this.robotController.getObjectLocation(command.getObjectName(), Direction.FORWARD);
				} catch (ObjectNotFoundException exc) {
					this.robotController.showException(exc);
				}
				movementPlan = this.robotController.getMovementPlanTo(location);
				this.robotController.move(movementPlan);
				this.robotController.checkObjectStatus(command.getObjectName());
				break;
			case SHUT_DOWN:
				this.robotController.shutDown();
				this.isActive = false;
			case NULL:
				break;
			default:
				break;
			}
			this.setStatus(RobotStatus.ACTIVE);	
	}

	public void importHomeFromXML(String xmlFilePath) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.IMPORT)
		.setFilePath(xmlFilePath);
		this.getCommands().add(command);
	}	
	
	public void goTo(Location destination) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_LOCATION)
		.setLocation(destination);
		this.getCommands().add(command);
	}
	
	public void goToStart() {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_START);
		this.getCommands().add(command);
	}

	public void goToRoom(String roomName) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_ROOM)
		.setRoomName(roomName);
		this.getCommands().add(command);
	}
	
	public void goToObject(String objectName) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_OBJECT)
		.setObjectName(objectName);
		this.getCommands().add(command);
	}
	
	public void goToObject(String objectName, Direction direction) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_OBJECT_WITH_DIRECTION)
		.setObjectName(objectName)
		.setDirection(direction);
		this.getCommands().add(command);
	}
	
	public void goToObject(String roomName, String objectName) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_OBJECT_IN_ROOM)
		.setRoomName(roomName)
		.setObjectName(objectName);
		this.getCommands().add(command);
	}
	
	public void goToObject(String roomName, String objectName, Direction direction) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_OBJECT_IN_ROOM_WITH_DIRECTION)
		.setRoomName(roomName)
		.setObjectName(objectName)
		.setDirection(direction);
		this.getCommands().add(command);
	}
	
	public void goToObject(String objectName, Direction direction, String nearObjectName) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_OBJECT_NEAR_OBJECT)
		.setObjectName(objectName)
		.setDirection(direction)
		.setNearObjectName(nearObjectName);
		this.getCommands().add(command);
	}
	
	public void moveObject(String roomName, String objectName, Location destination, Direction orientation) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.MOVE_OBJECT_TO_ROOM)
		.setRoomName(roomName)
		.setObjectName(objectName)
		.setLocation(destination)
		.setOrientation(orientation);
		this.getCommands().add(command);
	}
	
	public void makeActionOn(String objectName, Interaction action) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.MAKE_ACTION_ON_OBJECT)
		.setObjectName(objectName)
		.setAction(action);
		this.getCommands().add(command);
	}
	
	public void makeActionOn(String roomName, String objectName, Interaction action) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.MAKE_ACTION_ON_OBJECT_IN_ROOM)
		.setRoomName(roomName)
		.setObjectName(objectName)
		.setAction(action);
		this.getCommands().add(command);
	}
	
	public void shutDown() {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.SHUT_DOWN);
		this.getCommands().add(command);
	}

	public void checkStatus(String objectName, String status) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.CHECK_OBJECT_STATUS)
		.setObjectName(objectName)
		.setStatus(status);
		this.getCommands().add(command);
	}

	public void moveObject(String objectName, String roomName) {
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.MOVE_OBJECT_TO_ROOM)
		.setObjectName(objectName)
		.setRoomName(roomName);
		this.getCommands().add(command);
	}	

}
