package robomap.exception;

import robomap.model.vector.Location;

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
public class BlockingLockException extends Exception {
	
	private static final long serialVersionUID = -7547189507635080077L;
	
	private Location location;
	
	private static final String MESSAGE = "Found blocking lock in ";

	public BlockingLockException(Location location) {
		super(MESSAGE + location);
		this.setLocation(location);
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
