package robomap.control;

import robomap.model.object.Interaction;
import robomap.model.vector.Movement;

/**
 * @project robomap
 *
 * @package robomap.control
 *
 * @class MotorController
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class MotorController {
	
	private static final double MILLIS_FOR_MOVEMENT = 1000;
	private static final double MILLIS_FOR_ACTION = 1000;
	
	private GPSController gpsController;
	
	public MotorController(GPSController gpsController) {
		this.setGpsController(gpsController);
	}

	private GPSController getGpsController() {
		return this.gpsController;
	}

	private void setGpsController(GPSController gpsController) {
		this.gpsController = gpsController;
	}
	
	public void move(Movement movement) {
		float module = movement.getModule();
		double time = MILLIS_FOR_MOVEMENT * module;
		try {
			Thread.sleep((long) time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.getGpsController().setLocation(movement);
	}

	public void doAction(Interaction action) {
		try {
			Thread.sleep((long) MILLIS_FOR_ACTION);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
