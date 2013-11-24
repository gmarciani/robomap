package robomap.test;

import robomap.model.Robot;

public class Test {
	
	private static final String XML_PLANIMETRY_PATH = "test/sample_home.xml";

	public static void main(String[] args) {
		Robot robot = new Robot();
		robot.importHomeFromXML(XML_PLANIMETRY_PATH);
	}

}
