package robomap.database;

import robomap.model.object.Action;

public interface ActionDAO {

	public void saveAction(Action action);
	
	public void updateAction(Action action);
	
	public void deleteAction(Action action);

	public Action getAction(String actionName);

}
