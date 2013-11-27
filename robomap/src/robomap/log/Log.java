package robomap.log;

import java.sql.SQLException;

import robomap.model.object.Action;
import robomap.model.object.Object;
import robomap.model.robot.Movement;

public class Log {
	
	public static final void printSQLException(String className, String methodName, SQLException exc) {
		int errCode = exc.getErrorCode();
		String errMessage = exc.getMessage();
		System.out.println(className + " : " + methodName + " : " + errCode + " : " + errMessage);
	}
	
	public static final void printXMLException(String className, String methodName, Exception exc) {
		String errMessage = exc.getMessage();
		System.out.println(className + " : " + methodName + " : " + errMessage);
	}
	
	public static final void printMovement(Movement movement) {
		System.out.println("#Movement: " + movement.toString());
	}

	public static void printAction(Object object, Action action) {
		System.out.println("ACTION " + action.getName() + " ON OBJECT " + object.getName() + " --> " + action.getStatus());
	}

}
