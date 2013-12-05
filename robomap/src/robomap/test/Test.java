package robomap.test;

import robomap.model.robot.Robot;
import robomap.model.vector.Location;

public class Test {
	
	private static final String XML_PLANIMETRY_PATH = "test/sample_home.xml";

	public static void main(String[] args) {
		Robot mRobot = new Robot("mRobot");
		new Thread(mRobot).start();
		Robot yRobot = new Robot("yRobot");
		new Thread(yRobot).start();
		
		mRobot.importHomeFromXML(XML_PLANIMETRY_PATH);
		yRobot.importHomeFromXML(XML_PLANIMETRY_PATH);
		
		mRobot.goTo(new Location(9,9));
		yRobot.goTo(new Location(9,9));
		mRobot.shutDown();
		yRobot.shutDown();
	}

}
