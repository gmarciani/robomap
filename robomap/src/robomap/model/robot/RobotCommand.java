package robomap.model.robot;

import robomap.model.object.Interaction;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.model.robot
 *
 * @class RobotCommand
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class RobotCommand {
	
	private RobotOpcode opcode;
	private String filePath;
	private String robotName;
	private String homeName;
	private String roomName;
	private String objectName;
	private String nearObjectName;
	private Direction orientation;
	private Direction direction;
	private MovementPlan movementPlan;
	private Location location;
	private Interaction action;
	private String status;
	
	public RobotCommand() {
		this.setOpcode(RobotOpcode.NULL);
	}

	public RobotOpcode getOpcode() {
		return this.opcode;
	}

	public RobotCommand setOpcode(RobotOpcode opcode) {
		this.opcode = opcode;
		return this;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public RobotCommand setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}

	public String getRobotName() {
		return this.robotName;
	}

	public RobotCommand setRobotName(String robotName) {
		this.robotName = robotName;
		return this;
	}

	public String getHomeName() {
		return this.homeName;
	}

	public RobotCommand setHomeName(String homeName) {
		this.homeName = homeName;
		return this;
	}

	public String getRoomName() {
		return this.roomName;
	}

	public RobotCommand setRoomName(String roomName) {
		this.roomName = roomName;
		return this;
	}

	public String getObjectName() {
		return this.objectName;
	}

	public RobotCommand setObjectName(String objectName) {
		this.objectName = objectName;
		return this;
	}

	public Direction getOrientation() {
		return this.orientation;
	}

	public RobotCommand setOrientation(Direction orientation) {
		this.orientation = orientation;
		return this;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public RobotCommand setDirection(Direction direction) {
		this.direction = direction;
		return this;
	}

	public MovementPlan getMovementPlan() {
		return this.movementPlan;
	}

	public RobotCommand setMovementPlan(MovementPlan movementPlan) {
		this.movementPlan = movementPlan;
		return this;
	}

	public Location getLocation() {
		return this.location;
	}

	public RobotCommand setLocation(Location location) {
		this.location = location;
		return this;
	}
	
	public Interaction getAction() {
		return this.action;
	}

	public RobotCommand setAction(Interaction action) {
		this.action = action;
		return this;
	}
	
	@Override
	public String toString() {
		return "RobotCommand(" +
				this.getOpcode() + ", " + 
				this.getRobotName() + ", " + 
				this.getHomeName() + ", " + 
				this.getRoomName() + ", " + 
				this.getObjectName() + ", " + 
				this.getOrientation() + ", " + 
				this.getDirection() + ", " + 
				this.getLocation() + ", " + 
				this.getMovementPlan() + ", " + 
				this.getFilePath() + ", " + 
				this.getAction() + ")";
	}

	public String getNearObjectName() {
		return this.nearObjectName;
	}

	public RobotCommand setNearObjectName(String nearObjectName) {
		this.nearObjectName = nearObjectName;
		return this;
	}

	public String getStatus() {
		return this.status;
	}

	public RobotCommand setStatus(String status) {
		this.status = status;
		return this;
	}	

}
