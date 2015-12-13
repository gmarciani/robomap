package robomap.control;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;

import robomap.model.home.Home;
import robomap.model.message.InputMessage;
import robomap.model.message.Message;
import robomap.model.message.OutputMessage;
import robomap.model.object.Interaction;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;
import robomap.model.vector.Movement;

/**
 * @project robomap
 *
 * @package robomap.control
 *
 * @class DisplayController
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class DisplayController {

	private static DisplayController displayController;
	
	private static final Color INPUT_COLOR = Color.BLACK;
	private static final Color EXEC_COLOR = Color.BLUE;
	private static final Color MOVE_COLOR = Color.CYAN;
	private static final Color STATUS_COLOR = Color.GREEN;
	private static final Color ERROR_COLOR = Color.RED;
	private static final Color RESULT_COLOR = Color.YELLOW;
		
	private DisplayController() {
		AnsiConsole.systemInstall();
	}
	
	public static DisplayController getInstance() {
		if (displayController == null) {
			displayController = new DisplayController();
		}
		return displayController;
	}

	private synchronized int write(Message message) {
		return message.printMessage();
	}	
	
	public int showSelectHome(String robotName, List<Home> homes) {
		String body = "SELECT A HOME:";
		List<String> choices = new ArrayList<String>();
		for (Home home : homes) {
			choices.add(home.getName());
		}
		Message message = new InputMessage(robotName, body, choices, INPUT_COLOR);
		return this.write(message);
	}	
	
	public int showSelectAction(String robotName, List<Interaction> actions) {
		String body = "SELECT AN ACTION:";
		List<String> choices = new ArrayList<String>();
		for (Interaction action : actions) {
			choices.add(action.getName());
		}
		Message message = new InputMessage(robotName, body, choices, INPUT_COLOR);
		return this.write(message);
	}

	public void showStatus(String robotName, Home currentHome, Location currentLocation) {
		String body = "STATUS HOME " + currentHome.getName() + " " + 
						"IN (" + currentLocation.getX() + ";" + currentLocation.getY() + ") ";
		Message message = new OutputMessage(robotName, body, STATUS_COLOR);
		this.write(message);	
	}	
	
	public void showCommandImport(String robotName, String path) {
		String body = "EXEC IMPORT HOME " + path;
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}
	
	public void showCommandSetHome(String robotName, Home home) {
		String body = "EXEC SET HOME " + home.getName();
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}
	
	public void showCommandComputePathPlan(String robotName, Home home, Location source, Location destination) {
		String body = "EXEC COMPUTE PATHPLAN IN HOME " + home.getName() +  " " +
						"FROM (" + source.getX() + ";" + source.getY() + ") "  +
						"TO (" + destination.getX() + ";" + destination.getY() + ")";
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}
	
	public void showCommandComputeSelectivePathPlan(String robotName, Home home, Location source, Location destination, Location withoutLocation) {
		String body = "EXEC COMPUTE SELECTIVE PATHPLAN IN HOME " + home.getName() + " " +
						"FROM (" + source.getX() + ";" + source.getY() + ") " +
						"TO (" + destination.getX() + ";" + destination.getY() + ") " + 
						"WITHOUT " + withoutLocation;
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}
	
	public void showCommandComputeSelectivePathPlan(String robotName, Home home, Location source, Location destination, List<Location> withoutLocations) {
		String body = "EXEC COMPUTE SELECTIVE PATHPLAN IN HOME " + home.getName() + " " +
						"FROM (" + source.getX() + ";" + source.getY() + ") " +
						"TO (" + destination.getX() + ";" + destination.getY() + ") " + 
						"WITHOUT " + withoutLocations;
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}

	public void showCommandMovement(String robotName, Home home, Location prevLocation, Location nextLocation, Movement movement) {
		String body = "EXEC MOVE " + movement.getModule() + " " + movement.getDirection().getName() + " " +
						"IN HOME " + home.getName() + " " +
						"FROM (" + prevLocation.getX() + ";" + prevLocation.getY() + ") " +
						"TO (" + nextLocation.getX() +";" + nextLocation.getY() + ")" ;
		Message message = new OutputMessage(robotName, body, MOVE_COLOR);
		this.write(message);
	}

	public void showCommandShutDown(String robotName) {
		String body = "EXEC SHUTDOWN" ;
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}
	
	public void showError(String errorMessage) {
		Message message = new OutputMessage("warning", errorMessage, ERROR_COLOR);
		this.write(message);
	}	

	public void showCommandComputeSblockingLocation(String robotName, String homeName, Location location) {
		String body = "EXEC COMPUTE SBLOCKING LOCATION " + " " +
						"IN HOME " + homeName + " " +
						"FOR (" + location.getX() + ";" + location.getY() + ")";
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}

	public void showCommandComputeRoomLocation(String robotName, String homeName, String roomName) {
		String body = "EXEC COMPUTE ROOM LOCATION " + 
						"IN HOME " + homeName + " " + 
						"FOR ROOM " + roomName;
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}

	public void showCommandComputeObjectLocation(String robotName, String homeName, String objectName) {
		String body = "EXEC COMPUTE OBJECT LOCATION " + 
						"IN HOME " + homeName + " " + 
						"FOR OBJECT " + objectName;
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}

	public void showCommandComputeObjectLocation(String robotName, String homeName, String roomName, String objectName) {
		String body = "EXEC COMPUTE OBJECT LOCATION " + 
						"IN HOME " + homeName + " " + 
						"FOR OBJECT " + objectName + " " +
						"IN ROOM " + roomName;
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}

	public void showCommandComputeObjectLocation(String robotName, String homeName, String objectName, Direction direction) {
		String body = "EXEC COMPUTE OBJECT " + direction.getName() + " LOCATION " + 
						"IN HOME " + homeName + " " + 
						"FOR OBJECT " + objectName;
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}

	public void showCommandComputeObjectLocation(String robotName, String homeName, String roomName, String objectName, Direction direction) {
		String body = "EXEC COMPUTE OBJECT " + direction.getName() + " LOCATION " + 
						"IN HOME " + homeName + " " + 
						"FOR OBJECT " + objectName + " " +
						"IN ROOM " + roomName;
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}
	
	public void showCommandComputeObjectNearLocation(String robotName, String homeName, String objectName, Direction direction, String nearObjectName) {
		String body = "EXEC COMPUTE OBJECT LOCATION " + 
						"IN HOME " + homeName + " " + 
						"FOR OBJECT " + objectName + " " +
						"ON " + direction.getName() + " " + 
						"OF " + nearObjectName;
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}

	public void showCommandCheckObjectStatus(String robotName, String homeName, String objectName) {
		String body = "EXEC CHECK OBJECT STATUS " + 
				"IN HOME " + homeName + " " + 
				"FOR OBJECT " + objectName;
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}

	public void showRoomFound(String robotName, String homeName, String roomName, Location destination) {
		String body = "FOUND ROOM " + roomName + " " +
						"IN HOME " + homeName + " " +
						"IN (" + destination.getX() + ";" + destination.getY() + ")";
		Message message = new OutputMessage(robotName, body, RESULT_COLOR);
		this.write(message);
	}

	public void showObjectFound(String robotName, String homeName, String objectName, Location destination) {
		String body = "FOUND OBJECT " + objectName + " " +
						"IN HOME " + homeName + " " +
						"IN (" + destination.getX() + ";" + destination.getY() + ")";
		Message message = new OutputMessage(robotName, body, RESULT_COLOR);
		this.write(message);
	}	
	
	public void showBlockingLock(String robotName, String homeName, Location location) {
		String body = "STATUS FOUND BLOCKING LOCK " +
						"IN HOME " + homeName + " " +
						"IN (" + location.getX() + ";" + location.getY() + ")";
		Message message = new OutputMessage(robotName, body, ERROR_COLOR);
		this.write(message);		
	}

	public void showSblockingLocation(String robotName, String homeName, Location sblockingLocation) {
		String body = "FOUND SBLOCKING LOCATION " + 
						"IN HOME " + homeName + " " +
						"IN (" + sblockingLocation.getX() + ";" + sblockingLocation.getY() + ")";
		Message message = new OutputMessage(robotName, body, RESULT_COLOR);
		this.write(message);
	}

	public void showCommandMakeAction(String robotName, String homeName, Interaction action, String objectName) {
		String body = "EXEC ACTION " + action.getName() + " " + 
						"ON OBJECT " + objectName + " " +
						"IN HOME " + homeName;				
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}

	public void showCommandAddPayload(String robotName, String homeName, String objectName) {
		String body = "EXEC ADD PAYLOAD " + objectName + " " + 
				"IN HOME " + homeName;				
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}	

	public void showObjectStatus(String robotName, String homeName, String objectName, String status) {
		String body = "FOUND OBJECT STATUS " + status + " " +
				"FOR OBJECT " + objectName + " " +
				"IN HOME " + homeName;
		Message message = new OutputMessage(robotName, body, RESULT_COLOR);
		this.write(message);
	}
	
	public void showNewObjectLocation(String robotName, String homeName, String objectName, Location oldLocation, Location newLocation) {
		String body = "MOVED OBJECT " + objectName + " " +
				"IN HOME " + homeName + " " + 
				"FROM (" + oldLocation.getX() + ";" + oldLocation.getY() + ") " +
				"TO (" + newLocation.getX() + ";" + newLocation.getY() + ")";
		Message message = new OutputMessage(robotName, body, RESULT_COLOR);
		this.write(message);
	}

	

}
