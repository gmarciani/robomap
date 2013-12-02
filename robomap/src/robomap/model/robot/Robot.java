package robomap.model.robot;

import java.io.Serializable;

import robomap.control.RobotController;
import robomap.model.home.Home;
import robomap.model.object.Action;
import robomap.model.object.Object;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;

public class Robot implements Serializable {	

	private static final long serialVersionUID = -1567873543517965494L;
	
	private String name;
	private RobotController robotController;
	
	public Robot(String name) {
		this.setName(name);
		this.robotController = new RobotController(name);
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Home importHomeFromXML(String xmlFilePath) {
		Home home = this.robotController.importHomeFromXML(xmlFilePath);
		return home;
	}	
	
	public void setHome(Home home) {
		this.robotController.setCurrentHome(home);
	}
	
	public void selectHome() {
		this.robotController.selectHome();
	}
	
	public void goTo(Location destination) {
		MovementPlan movementPlan = this.robotController.getMovementPlanTo(destination);
		this.robotController.move(movementPlan);
	}
	
	public void goToStart() {
		Location location = this.robotController.getStartLocation();
		MovementPlan movementPlan = this.robotController.getMovementPlanTo(location);
		this.robotController.move(movementPlan);
	}

	public void goTo(String roomName) {
		Location location = this.robotController.getRoomLocation(roomName);
		MovementPlan movementPlan = this.robotController.getMovementPlanTo(location);
		this.robotController.move(movementPlan);
	}
	
	public void goTo(String roomName, String objectName) {
		Location location = this.robotController.getObjectLocation(roomName, objectName);
		MovementPlan movementPlan = this.robotController.getMovementPlanTo(location);
		this.robotController.move(movementPlan);
	}
	
	public void goTo(String roomName, String objectName, Direction direction) {
		Location location = this.robotController.getObjectLocation(roomName, objectName, direction);
		MovementPlan movementPlan = this.robotController.getMovementPlanTo(location);
		this.robotController.move(movementPlan);		
	}
	
	public void moveObject(String roomName, String objectName, Location destination, Direction orientation) {
		Object object = this.robotController.getObject(roomName, objectName);
		Location objectLocation = object.getLocation();
		MovementPlan movementPlanToObject = this.robotController.getMovementPlanTo(objectLocation);
		this.robotController.move(movementPlanToObject);
		this.robotController.addPayload(object);
		MovementPlan movementPlanToDestination = this.robotController.getMovementPlanTo(destination);
		this.robotController.move(movementPlanToDestination);
		this.robotController.setObjectOrientation(roomName, objectName, orientation);
		this.robotController.releasePayload();
	}
	
	public void makeActionOn(String roomName, String objectName) {
		Object object = this.robotController.getObject(roomName, objectName);
		Action action = this.robotController.selectAction(object);
		Location location = object.getLocation();
		MovementPlan movementPlan = this.robotController.getMovementPlanTo(location);
		this.robotController.move(movementPlan);		
		this.robotController.doAction(object, action);
	}

}
