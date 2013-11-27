package robomap.model.object;

import java.io.Serializable;

public class Action implements Serializable {
	
	private static final long serialVersionUID = 3704099534887332792L;
	
	private String name;
	private String status;
	
	public Action(String name, String status) {
		this.setName(name);
		this.setStatus(status);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "Action(" + 
				this.getName() + ", " + 
				this.getStatus() + ")";
	}

}
