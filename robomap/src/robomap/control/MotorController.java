package robomap.control;

import robomap.model.robot.Movement;

public class MotorController {
	
	private static final double SECONDS_FOR_MOVEMENT = 1000;
	
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
}
