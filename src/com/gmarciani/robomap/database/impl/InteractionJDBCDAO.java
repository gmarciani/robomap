package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import robomap.database.DatabaseDebug;
import robomap.database.InteractionDAO;
import robomap.database.ConnectionManager;
import robomap.log.Log;
import robomap.model.object.Interaction;

/**
 * @project robomap
 *
 * @package robomap.database.impl
 *
 * @class InteractionJDBCDAO
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class InteractionJDBCDAO implements InteractionDAO {	
	
	private static InteractionJDBCDAO actionDAO;
	
	private ConnectionManager connectionManager;
	
	private static final String SQL_INSERT_INTERACTION = "INSERT INTO Interaction (iname, istatus) VALUES (?, ?)";
	
	private static final String SQL_INSERT_INTERACTION_ON_OBJECT = "INSERT INTO InteractionOn (iname, oname) VALUES (?, ?)";
	
	private InteractionJDBCDAO() {
		this.connectionManager = JDBCConnectionManager.getInstance();
	}	
	
	public static InteractionJDBCDAO getInstance() {
		if(actionDAO == null) {
			actionDAO = new InteractionJDBCDAO();
		}
		return actionDAO;
	}
	
	@Override
	public synchronized void saveInteraction(Interaction interaction) {
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(SQL_INSERT_INTERACTION);
			stmt.setString(1, interaction.getName());
			stmt.setString(2, interaction.getStatus());
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			stmt.executeUpdate();
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);
		}
	}

	@Override
	public synchronized void saveInteraction(String objectName, Interaction interaction) {	
		this.saveInteraction(interaction);
		
		Connection connection = this.connectionManager.getConnection();
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(SQL_INSERT_INTERACTION_ON_OBJECT);
			stmt.setString(1, interaction.getName());
			stmt.setString(2, objectName);
			if (DatabaseDebug.D) Log.printSQLStatement(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), stmt);
			stmt.executeUpdate();
		} catch (SQLException exc) {
			if (DatabaseDebug.D) Log.printSQLException(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), exc);
		} finally {
			this.connectionManager.close(connection);
		}	
	}		

}
