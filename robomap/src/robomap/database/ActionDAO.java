package robomap.database;

import java.util.List;

import robomap.model.object.Action;
import robomap.model.object.Object;

public interface ActionDAO {

	List<Action> getActions(Object object);

}
