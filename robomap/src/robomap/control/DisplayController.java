package robomap.control;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;

import robomap.model.home.Home;
import robomap.model.message.InputMessage;
import robomap.model.message.Message;
import robomap.model.message.OutputMessage;
import robomap.model.object.Action;
import robomap.model.vector.Location;
import robomap.model.vector.Movement;

public class DisplayController {

	private static DisplayController displayController;
	
	private static final Color INPUT_COLOR = Color.BLACK;
	private static final Color EXEC_COLOR = Color.BLUE;
	private static final Color MOVE_COLOR = Color.CYAN;
	private static final Color STATUS_COLOR = Color.GREEN;
		
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
	
	public int showSelectAction(String robotName, List<Action> actions) {
		String body = "SELECT AN ACTION:";
		List<String> choices = new ArrayList<String>();
		for (Action action : actions) {
			choices.add(action.getName());
		}
		Message message = new InputMessage(robotName, body, choices, INPUT_COLOR);
		return this.write(message);
	}

	public void showStatus(String robotName, Home currentHome, Location currentLocation) {
		String body = "STATUS HOME " + currentHome.getName() + " " + "IN (" + currentLocation.getX() + ";" + currentLocation.getY() + ") ";
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
	
	public void showCommadComputePathPlan(String robotName, Home home, Location source, Location destination) {
		String body = "EXEC COMPUTE PATHPLAN FOR HOME " + home.getName() + " FROM (" + source.getX() + ";" + source.getY() + ") TO (" + destination.getX() + ";" + destination.getY() + ")";
		Message message = new OutputMessage(robotName, body, EXEC_COLOR);
		this.write(message);
	}

	public void showCommandMovement(String robotName, Home home, Location prevLocation, Location nextLocation, Movement movement) {
		String body = "EXEC MOVE " + movement.getModule() + " " + movement.getDirection().getName() + " IN " + home.getName() + " FROM (" + prevLocation.getX() + ";" + prevLocation.getY() + ") TO (" + nextLocation.getX() +";" + nextLocation.getY() + ")" ;
		Message message = new OutputMessage(robotName, body, MOVE_COLOR);
		this.write(message);
	}

}
