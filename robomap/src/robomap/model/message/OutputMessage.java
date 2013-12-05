package robomap.model.message;

import java.util.Date;
import static org.fusesource.jansi.Ansi.*;

public class OutputMessage implements Message {
	
	private String robotName;
	private long time;
	private String body;
	private Color foreground;	
	
	public OutputMessage(String robotName, String body, Color foreground) {
		this.setRobotName(robotName);
		this.setTime(new Date().getTime());
		this.setBody(body);
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
		String message = this.getHeader() + "\t" + this.getBody();
		System.out.println(ansi().fg(this.getForeground()).a(message).reset());		
		return -1;
	}
	

}
