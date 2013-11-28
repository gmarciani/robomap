package robomap.control;

import java.util.ArrayList;
import java.util.List;

import robomap.model.base.Location;
import robomap.model.home.Home;
import robomap.model.object.Action;

public class DisplayController {
	
	private interface Message {
		
		public int printMessage();
		
	}
	
	private static class OutputMessage implements Message {
		
		private String robotName;
		private String body;
		
		public OutputMessage(String robotName, String body) {
			this.setRobotName(robotName);
			this.setBody(body);
		}

		public String getRobotName() {
			return this.robotName;
		}

		private void setRobotName(String robotName) {
			this.robotName = robotName;
		}

		public String getBody() {
			return this.body;
		}

		private void setBody(String body) {
			this.body = body;
		}

		@Override
		public int printMessage() {
			System.out.println("[" + this.getRobotName() + "]\t" + this.getBody());		
			return -1;
		}
	}
	
	private static class InputMessage implements Message {
		
		private String robotName;
		private String body;
		private List<String> choices;
		
		public InputMessage(String robotName, String body, List<String> choices) {
			this.setRobotName(robotName);
			this.setBody(body);
			this.setChoices(choices);
		}

		public String getRobotName() {
			return this.robotName;
		}

		private void setRobotName(String robotName) {
			this.robotName = robotName;
		}

		public String getBody() {
			return this.body;
		}

		private void setBody(String body) {
			this.body = body;
		}
		
		public List<String> getChoices() {
			return this.choices;
		}

		private void setChoices(List<String> choices) {
			this.choices = choices;
		}

		@Override
		public int printMessage() {			
			int selection = -1;
			
			while(selection < this.getChoices().size() || selection >= this.getChoices().size()) {		
				System.out.println("[" + this.getRobotName() + "]\t" + this.getBody());
				for (String choice : this.getChoices()) {
					System.out.println(this.getChoices().indexOf(choice) + ") " + choice);
				}
				selection = Integer.valueOf(System.console().readLine("Enter choice: "));
			}
			return selection;			
		}		
	}

	private static DisplayController displayController;
		
	private DisplayController() {
		
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

	public void showImportedHome(String robotName, Home home) {
		String body = "IMPORTED HOME " + home.getName();
		Message message = new OutputMessage(robotName, body);
		this.write(message);
	}	
	
	public int selectHome(String robotName, List<Home> homes) {
		String body = "Select a home:";
		List<String> choices = new ArrayList<String>();
		for (Home home : homes) {
			choices.add(home.getName());
		}
		Message message = new InputMessage(robotName, body, choices);
		return this.write(message);
	}	
	
	public int selectAction(String robotName, List<Action> actions) {
		String body = "Select an action:";
		List<String> choices = new ArrayList<String>();
		for (Action action : actions) {
			choices.add(action.getName());
		}
		Message message = new InputMessage(robotName, body, choices);
		return this.write(message);
	}

	public void showCurrentStatus(String robotName, Home currentHome, Location currentLocation) {
		String body = "Status: Home: " + currentHome.getName() + ", Location: (" + currentLocation.getX() + "; " + currentLocation.getY() + ")";
		Message message = new OutputMessage(robotName, body);
		this.write(message);		
	}	

}
