package robomap.model.robot;

/**
 * @project robomap
 *
 * @package robomap.model.robot
 *
 * @class RobotOpcode
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public enum RobotOpcode {
	
	IMPORT,
	GOTO_START,
	GOTO_LOCATION,
	GOTO_ROOM,
	GOTO_OBJECT,
	GOTO_OBJECT_IN_ROOM,
	GOTO_OBJECT_WITH_DIRECTION,
	GOTO_OBJECT_IN_ROOM_WITH_DIRECTION,
	GOTO_OBJECT_NEAR_OBJECT,
	MOVE_OBJECT_TO_ROOM,
	MAKE_ACTION_ON_OBJECT,
	MAKE_ACTION_ON_OBJECT_IN_ROOM,
	CHECK_OBJECT_STATUS,
	SHUT_DOWN,
	NULL;
	
	private RobotOpcode() {}

}
