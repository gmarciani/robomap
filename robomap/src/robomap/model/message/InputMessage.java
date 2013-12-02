package robomap.model.message;

import java.util.Date;
import java.util.List;

import static org.fusesource.jansi.Ansi.*;

public class InputMessage implements Message {

	private String robotName;
	private long time;
	private String body;
	private List<String> choices;
	private Color foreground;	
	
	public InputMessage(String robotName, String body, List<String> choices, Color foreground) {
		this.setRobotName(robotName);
		this.setTime(new Date().getTime());
		this.setBody(body);
		this.setChoices(choices);
		this.setForeground(foreground);			
	}

	public String getRobotName() {
		return this.robotName;
	}

	private void setRobotName(String robotName) {
		this.robotName = robotName;
	}

	public long getTime() {
		return this.time;
	}

	private void setTime(long time) {
		this.time = time;
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
	
	public Color getForeground() {
		return this.foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}
	
	private String getHeader() {
		return String.format("[%s %2$tH:%2$tM:%2$tS:%2$tL]", this.getRobotName(), this.getTime());
	}

	@Override
	public int printMessage() {			
		int selection = -1;
		
		String message = this.getHeader() + "\t" + this.getBody();
		String list = null;
		String input = "Enter a choice: ";
		
		for (String choice : this.getChoices()) {
			list += this.getChoices().indexOf(choice) + ") " + choice;
		}
		
		while(selection < this.getChoices().size() || selection >= this.getChoices().size()) {		
			System.out.println(ansi().fg(this.getForeground()).a(message));
			System.out.println(ansi().fg(this.getForeground()).a(list));
			selection = Integer.valueOf(System.console().readLine(input));
		}
		return selection;			
	}

	
}
