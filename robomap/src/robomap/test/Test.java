package robomap.test;

import robomap.model.base.Location;
import robomap.model.robot.Robot;

public class Test {
	
	private static final String XML_PLANIMETRY_PATH = "src/robomap/test/sample_home.xml";

	public static void main(String[] args) {
		Robot mRobot = new Robot("mRobot");
		mRobot.importHomeFromXML(XML_PLANIMETRY_PATH);
		mRobot.selectHome();
		mRobot.goTo(new Location(9,9));
		mRobot.goTo(new Location(0,9));
		mRobot.goToStart();
	}

}
