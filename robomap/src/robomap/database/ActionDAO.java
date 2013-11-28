package robomap.database;

import java.util.List;

import robomap.model.object.Action;
import robomap.model.object.Object;

public interface ActionDAO {

	public void saveAction(Action action);
	
	public void updateAction(Action action);
	
	public void deleteAction(Action action);

	public Action getAction(String actionName);

	public List<Action> getActions(Object object);

}
