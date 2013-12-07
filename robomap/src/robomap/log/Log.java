package robomap.log;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @project robomap
 *
 * @package robomap.log
 *
 * @class Log
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class Log {
	
	public static final void printSQLStatement(String className, String methodName, PreparedStatement stmt) {
		System.out.println("CLASS: " + className + " METHOD: " + methodName + " SQL: " + stmt);
	}
	
	public static final void printSQLException(String className, String methodName, SQLException exc) {
		System.err.println(className + " : " + methodName + " : " + exc.getErrorCode() + " : " + exc.getMessage());
	}

	public static void printException(String className, String methodName, Exception exc) {
		System.out.println("CLASS: " + className + " METHOD: " + methodName + " EXCEPTION: " + exc.getMessage());
	}

}
