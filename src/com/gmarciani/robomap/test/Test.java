package robomap.test;

import robomap.model.object.Interaction;
import robomap.model.robot.Robot;
import robomap.model.vector.Direction;
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
		
		mRobot.goTo(new Location(11,3));
		yRobot.goTo(new Location(11,3));
		mRobot.goTo(new Location(8,8));
		yRobot.goTo(new Location(8,8));
		mRobot.goToStart();
		yRobot.goToStart();
		yRobot.shutDown();
		
		mRobot.goToRoom("cucina");
		
		mRobot.goToObject("televisione");
		
		mRobot.goToObject("divano", Direction.BACKWARD);
		
		mRobot.goToObject("camera_da_letto_2", "comodino");		
		
		mRobot.goToObject("salotto", "tavolo", Direction.FORWARD);
		
		mRobot.makeActionOn("televisione", Interaction.SWITCH_ON);
		
		mRobot.makeActionOn("cucina", "frigo", Interaction.CLOSE);
		
		mRobot.checkStatus("frigo", Interaction.CLOSE.getStatus());
		
		mRobot.moveObject("cappelliera", "salotto");
		
		mRobot.goToObject("comodino", Direction.RIGHT, "letto_matrimoniale");		
		
		mRobot.shutDown();
	}

}
