package robomap.model.robot;

public enum Opcode {
	
	IMPORT,
	GOTO_START,
	GOTO_LOCATION,
	GOTO_ROOM,
	GOTO_OBJECT,
	GOTO_OBJECT_DIRECTION,
	MAKE_ACTION,
	MOVE_OBJECT,
	SHUT_DOWN,
	NULL;
	
	private Opcode() {}

}
