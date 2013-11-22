package robomap.log;

import java.sql.SQLException;

import robomap.model.Movement;

public class Log {
	
	public static final void printSQLException(String className, String methodName, SQLException exc) {
		int errCode = exc.getErrorCode();
		String errMessage = exc.getMessage();
		System.out.println(className + " : " + methodName + " : " + errCode + " : " + errMessage);
	}
	
	public static final void printMovement(Movement movement) {
		System.out.println("#Movement: " + movement.toString());
	}

}
