package robomap.model.robot;


import java.util.ArrayList;
import java.util.List;

import robomap.control.RobotController;
import robomap.model.home.Home;
import robomap.model.object.Action;
import robomap.model.object.Object;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;

public class Robot extends Thread {	
	
	private static final boolean D = false;
	
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
		if(D) System.out.println("setStatus " + status);
		this.status = status;
	}
	
	private List<RobotCommand> getCommands() {
		return this.commands;
	}

	private void setCommands(List<RobotCommand> commands) {
		this.commands = commands;
	}
	
	public boolean isActive() {
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
			switch(command.getOpcode()) {
			case IMPORT:
				Home home = this.robotController.importHomeFromXML(command.getFilePath());
				this.robotController.setCurrentHome(home);
				break;
			case GOTO_START:
				Location startLocation = this.robotController.getStartLocation();
				MovementPlan movementPlanToStart = this.robotController.getMovementPlanTo(startLocation);
				this.robotController.move(movementPlanToStart);
				break;
			case GOTO_LOCATION:
				MovementPlan movementPlanToDestination = this.robotController.getMovementPlanTo(command.getLocation());
				this.robotController.move(movementPlanToDestination);
				break;
			case GOTO_ROOM:
				Location roomLocation = this.robotController.getRoomLocation(command.getRoomName());
				MovementPlan movementPlanToRoom = this.robotController.getMovementPlanTo(roomLocation);
				this.robotController.move(movementPlanToRoom);
				break;
			case GOTO_OBJECT:
				Location objectLocation = this.robotController.getObjectLocation(command.getRoomName(), command.getObjectName());
				MovementPlan movementPlanToObject = this.robotController.getMovementPlanTo(objectLocation);
				this.robotController.move(movementPlanToObject);
				break;
			case GOTO_OBJECT_DIRECTION:
				Location objectLocationDirection = this.robotController.getObjectLocation(command.getRoomName(), command.getObjectName(), command.getRelativePositition());
				MovementPlan movementPlanToObjectDirection = this.robotController.getMovementPlanTo(objectLocationDirection);
				this.robotController.move(movementPlanToObjectDirection);
				break;
			case MOVE_OBJECT:
				Object objectToMove = this.robotController.getObject(command.getRoomName(), command.getObjectName());
				objectLocation = objectToMove.getLocation();
				movementPlanToObject = this.robotController.getMovementPlanTo(objectLocation);
				this.robotController.move(movementPlanToObject);
				this.robotController.addPayload(objectToMove);
				movementPlanToDestination = this.robotController.getMovementPlanTo(command.getLocation());
				this.robotController.move(movementPlanToDestination);
				this.robotController.setObjectOrientation(command.getRoomName(), command.getObjectName(), command.getOrientation());
				this.robotController.releasePayload();
				break;
			case MAKE_ACTION:
				Object objectToActOn = this.robotController.getObject(command.getRoomName(), command.getObjectName());
				Location location = objectToActOn.getLocation();
				MovementPlan movementPlan = this.robotController.getMovementPlanTo(location);
				this.robotController.move(movementPlan);		
				this.robotController.doAction(objectToActOn, command.getAction());
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
		if(D) System.out.println("importHomeFromXML");
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.IMPORT)
		.setFilePath(xmlFilePath);
		this.getCommands().add(command);
	}	
	
	public void goTo(Location destination) {
		if(D) System.out.println("goToLocation");
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_LOCATION)
		.setLocation(destination);
		this.getCommands().add(command);
	}
	
	public void goToStart() {
		if(D) System.out.println("goToStart");
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_START);
		this.getCommands().add(command);
	}

	public void goTo(String roomName) {
		if(D) System.out.println("goToRoom");
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_ROOM)
		.setRoomName(roomName);
		this.getCommands().add(command);
	}
	
	public void goTo(String roomName, String objectName) {
		if(D) System.out.println("goToObject");
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_OBJECT)
		.setRoomName(roomName)
		.setObjectName(objectName);
		this.getCommands().add(command);
	}
	
	public void goTo(String roomName, String objectName, Direction direction) {
		if(D) System.out.println("goToObjectDirection");
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.GOTO_OBJECT_DIRECTION)
		.setRoomName(roomName)
		.setObjectName(objectName)
		.setRelativePositition(direction);
		this.getCommands().add(command);	
	}
	
	public void moveObject(String roomName, String objectName, Location destination, Direction orientation) {
		if(D) System.out.println("moveObject");
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.MOVE_OBJECT)
		.setRoomName(roomName)
		.setObjectName(objectName)
		.setLocation(destination)
		.setOrientation(orientation);
		this.getCommands().add(command);
	}
	
	public void makeActionOn(String roomName, String objectName, Action action) {
		if(D) System.out.println("makeActionOn");
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.MAKE_ACTION)
		.setRoomName(roomName)
		.setObjectName(objectName)
		.setAction(action);
		this.getCommands().add(command);
	}
	
	public void shutDown() {
		if(D) System.out.println("shutDown");
		RobotCommand command = new RobotCommand()
		.setOpcode(RobotOpcode.SHUT_DOWN);
		this.getCommands().add(command);
	}	

}
