package robomap.model.robot;

import java.io.Serializable;

import robomap.control.RobotController;
import robomap.model.base.Direction;
import robomap.model.base.Location;
import robomap.model.graph.PathPlan;
import robomap.model.home.Home;
import robomap.model.home.Room;
import robomap.model.object.Action;
import robomap.model.object.Object;

public class Robot implements Serializable {	

	private static final long serialVersionUID = -1567873543517965494L;
	
	private String name;
	private static RobotController robotController;
	
	public Robot(String name) {
		this.setName(name);
		robotController = new RobotController(name);
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void importHomeFromXML(String xmlFilePath) {
		Home home = robotController.importHomeFromXML(xmlFilePath);
		robotController.setCurrentHome(home);
	}	
	
	public void selectHome() {
		robotController.selectHome();
	}
	
	public Home getCurrentHome() {
		return robotController.getCurrentHome();
	}
	
	public Location getCurrentLocation() {
		return robotController.getCurrentLocation();
	}
	
	public void move(Movement movement) {
		robotController.move(movement);
	}
	
	public void goTo(Location destination) {
		PathPlan pathPlan = robotController.getPathPlanTo(destination);
		robotController.move(pathPlan);
	}
	
	public void goToStart() {
		Location location = robotController.getStartLocation();
		PathPlan pathPlan = robotController.getPathPlanTo(location);
		robotController.move(pathPlan);
	}

	public void goTo(Room room) {
		Location location = robotController.getLocation(room);
		PathPlan pathPlan = robotController.getPathPlanTo(location);
		robotController.move(pathPlan);
	}
	
	public void goTo(Object object) {
		Location location = robotController.getLocation(object);
		PathPlan pathPlan = robotController.getPathPlanTo(location);
		robotController.move(pathPlan);
	}
	
	public void goTo(Object object, Direction direction) {
		Location location = robotController.getLocation(object, direction);
		PathPlan pathPlan = robotController.getPathPlanTo(location);
		robotController.move(pathPlan);		
	}
	
	public void moveObject(Object object, Location destination) {
		Location location = robotController.getLocation(object);
		PathPlan pathPlanToObject = robotController.getPathPlanTo(location);
		robotController.move(pathPlanToObject);
		robotController.addPayload(object);
		PathPlan pathPlantoDestination = robotController.getPathPlanTo(destination);
		robotController.move(pathPlantoDestination);
		robotController.releasePayload();
	}
	
	public void makeActionOn(Object object) {
		Action action = robotController.selectAction(object);
		Location location = robotController.getLocation(object, action);
		PathPlan pathPlan = robotController.getPathPlanTo(location);
		robotController.move(pathPlan);		
		robotController.doAction(object, action);
	}

}
