package robomap.model.object;

import java.io.Serializable;

/**
 * @project robomap
 *
 * @package robomap.model.object
 *
 * @class Interaction
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public enum Interaction implements Serializable {
	
	SWITCH_ON("SWITCH_ON", "ON"),
	SWITCH_OFF("SWITCH_OFF", "OFF");
	
	private final String name;
	private final String status;
	
	Interaction(final String name, final String status) {
		this.name = name;
		this.status = status;
	}

	public String getName() {
		return this.name;
	}

	public String getStatus() {
		return this.status;
	}
	
	@Override
	public String toString() {
		return "Interaction(" + 
				this.getName() + ", " + 
				this.getStatus() + ")";
	}

}
