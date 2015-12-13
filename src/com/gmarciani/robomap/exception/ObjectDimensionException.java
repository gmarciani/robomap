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
public class ObjectDimensionException extends NotFoundException {
	
	private static final long serialVersionUID = -7547189507635080077L;
	
	private String objectName;
	
	private static final String MESSAGE= "Object too large ";

	public ObjectDimensionException(String objectName) {
		super(MESSAGE + objectName);
		this.setObjectName(objectName);
	}

	public String getObjectName() {
		return this.objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

}
