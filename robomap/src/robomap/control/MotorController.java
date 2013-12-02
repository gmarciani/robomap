package robomap.control;

import robomap.model.object.Action;
import robomap.model.vector.Movement;

public class MotorController {
	
	private static final double SECONDS_FOR_MOVEMENT = 1000;
	private static final double SECONDS_FOR_ACTION = 2000;
	
	private GPSController gpsController;
	
	public MotorController(GPSController gpsController) {
		this.setGpsController(gpsController);
	}
	
	public void move(Movement movement) {
		float module = movement.getModule();
		double time = SECONDS_FOR_MOVEMENT * module;
		try {
			Thread.sleep((long) time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.getGpsController().setLocation(movement);
	}

	private GPSController getGpsController() {
		return this.gpsController;
	}

	private void setGpsController(GPSController gpsController) {
		this.gpsController = gpsController;
	}

	public void doAction(Action action) {
		try {
			Thread.sleep((long) SECONDS_FOR_ACTION);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
