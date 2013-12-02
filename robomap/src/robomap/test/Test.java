package robomap.test;

import robomap.model.home.Home;
import robomap.model.robot.Robot;
import robomap.model.vector.Location;

public class Test {
	
	private static final String XML_PLANIMETRY_PATH = "test/sample_home.xml";

	public static void main(String[] args) {
		Robot mRobot = new Robot("mRobot");
		Home mHome = mRobot.importHomeFromXML(XML_PLANIMETRY_PATH);
		mRobot.setHome(mHome);
		mRobot.goTo(new Location(9,9));
		mRobot.goTo(new Location(0,9));
		mRobot.goToStart();
		
		Robot yRobot = new Robot("yRobot");
		Home yHome = mRobot.importHomeFromXML(XML_PLANIMETRY_PATH);
		yRobot.setHome(yHome);
		yRobot.goTo(new Location(9,9));
		yRobot.goTo(new Location(0,9));
		yRobot.goToStart();
	}

}
