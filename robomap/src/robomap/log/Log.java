package robomap.log;

import java.sql.SQLException;

public class Log {
	
	public static final void printSQLException(String className, String methodName, SQLException exc) {
		int errCode = exc.getErrorCode();
		String errMessage = exc.getMessage();
		System.err.println(className + " : " + methodName + " : " + errCode + " : " + errMessage);
	}
	
	public static final void printXMLException(String className, String methodName, Exception exc) {
		String errMessage = exc.getMessage();
		System.err.println(className + " : " + methodName + " : " + errMessage);
	}

}
