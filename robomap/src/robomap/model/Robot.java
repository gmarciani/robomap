package robomap.model;

import robomap.control.RobotController;
import robomap.log.Log;

public class Robot {
	
	private String home;
	private Location location;
	
	private static RobotController controller;
	
	public Robot() {
		controller = RobotController.getInstance();
	}
	
	public void importHomeFromXML(String path) {
		controller.importHomeFromXML(path);
	}
	
	public String getHome() {
		return this.home;
	}

	public void setHome(String home) {
		controller.loadHome(home);
		this.home = home;
	}	

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}	
	
	private void move(Movement movement) {
		int currX = this.getLocation().getX();
		int currY = this.getLocation().getY();
		int moveX = (movement.getDirection() == Direction.RIGHT) ? 1 : -1;
		int moveY = (movement.getDirection() == Direction.FORWARD) ? 1 : -1;
		Location newLocation = new Location(currX + moveX, currY + moveY);
		this.setLocation(newLocation);
		Log.printMovement(movement);
	}
	
	private void move(PathPlan pathPlan) {
		for(Movement movement : pathPlan.getMovements()) {
			this.move(movement);
		}		
	}
	
	public void goToStart() {
		Location location = controller.getStartLocation();
		PathPlan pathPlan = controller.getPathPlan(this.getLocation(), location);
		this.move(pathPlan);
	}

	public void goTo(Room room) {
		Location location = controller.getLocation(room);
		PathPlan pathPlan = controller.getPathPlan(this.getLocation(), location);
		this.move(pathPlan);
	}
	
	public void goTo(Object object) {
		Location location = controller.getLocation(object);
		PathPlan pathPlan = controller.getPathPlan(this.getLocation(), location);
		this.move(pathPlan);
	}

}
