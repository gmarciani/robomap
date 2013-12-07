package robomap.model.robot;

/**
 * @project robomap
 *
 * @package robomap.model.robot
 *
 * @class RobotStatus
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public enum RobotStatus {
	
	INITIALIZATION,
	ACTIVE,
	COMMAND,
	EXECUTION,
	SWITCH_OFF;
	
	private RobotStatus() {}

}
