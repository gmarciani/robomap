package robomap.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import robomap.database.ActionDAO;
import robomap.database.ConnectionManager;
import robomap.log.Log;
import robomap.model.object.Action;
import robomap.model.object.Object;

public class ActionJDBCDAO implements ActionDAO {
	
	private static ConnectionManager connectionManager;
	private static ActionJDBCDAO actionDAO;
	
	private ActionJDBCDAO() {}	
	
	public static ActionJDBCDAO getInstance() {
		if(actionDAO == null) {
			actionDAO = new ActionJDBCDAO();
		}
		return actionDAO;
	}

	@Override
	public List<Action> getActions(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

}
