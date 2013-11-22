package robomap.database;

import robomap.model.Location;
import robomap.model.Object;

public interface ObjectDAO {

	Location findLocation(Object object);

}
