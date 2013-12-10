package robomap.exception;


/**
 * @project robomap
 *
 * @package robomap.exception
 *
 * @class BlockingLockException
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class RoomNotFoundException extends NotFoundException {
	
	private static final long serialVersionUID = -7547189507635080077L;
	
	private String roomName;
	
	private static final String MESSAGE = "Room not found ";

	public RoomNotFoundException(String roomName) {
		super(MESSAGE + roomName);
		this.setRoomName(roomName);
	}

	public String getRoomName() {
		return this.roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

}
