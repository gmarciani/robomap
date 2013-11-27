package robomap.model.robot;

import java.io.Serializable;
import java.util.List;

import robomap.control.RobotController;
import robomap.model.base.Direction;
import robomap.model.base.Location;
import robomap.model.graph.PathPlan;
import robomap.model.home.Room;
import robomap.model.object.Object;

public class Robot implements Serializable {	

	private static final long serialVersionUID = -1567873543517965494L;
	
	private static RobotController robotController;
	
	public Robot() {
		robotController = RobotController.getInstance();
	}
	
	public void importHomeFromXML(String xmlFilePath) {
		robotController.importHomeFromXML(xmlFilePath);
	}	
	
	public void move(PathPlan pathPlan) {
		for(Movement movement : pathPlan.getMovements()) {
			robotController.move(movement);
		}		
	}
	
	public void goToStart() {
		Location location = robotController.getStartLocation();
		PathPlan pathPlan = robotController.getPathPlanTo(location);
		this.move(pathPlan);
	}

	public void goTo(Room room) {
		Location location = robotController.getLocation(room);
		PathPlan pathPlan = robotController.getPathPlanTo(location);
		this.move(pathPlan);
	}
	
	public void goTo(Object object) {
		Location location = robotController.getLocation(object);
		PathPlan pathPlan = robotController.getPathPlanTo(location);
		this.move(pathPlan);
	}
	
	public void goTo(Object object, Direction direction) {
		Location location = robotController.getLocation(object, direction);
		PathPlan pathPlan = robotController.getPathPlanTo(location);
		this.move(pathPlan);		
	}
	
	public void makeActionOn(Object object) {
		List<String> actions = robotController.getAvailableActions(object);
		int chosenAction = robotController.getUserChoice(actions);
		String action = actions.get(chosenAction);
		Location location = robotController.getLocation(object, action);
		PathPlan pathPlan = robotController.getPathPlanTo(location);
		this.move(pathPlan);		
		robotController.doAction(object, action);
	}

}
