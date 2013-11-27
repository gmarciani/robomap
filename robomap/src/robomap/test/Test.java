package robomap.test;

import java.util.List;

import robomap.control.RobotController;
import robomap.model.base.Location;
import robomap.model.graph.PathPlan;
import robomap.model.home.Home;

public class Test {
	
	private static final String XML_PLANIMETRY_PATH = "src/robomap/test/sample_home.xml";

	public static void main(String[] args) {
		RobotController robotController = RobotController.getInstance();
		List<Home> listHomes = robotController.importHomeFromXML(XML_PLANIMETRY_PATH);
		for (Home home : listHomes) {
			robotController.setCurrentHome(home);
		}
		robotController.setCurrentLocation(new Location(0,0));
		PathPlan plan = robotController.getPathPlanTo(new Location(9,9));
		System.out.println("#PATH PLAN: " + plan.toString());
	}

}
