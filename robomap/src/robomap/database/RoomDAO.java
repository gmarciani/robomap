package robomap.database;

import robomap.model.Location;
import robomap.model.Room;

public interface RoomDAO {

	Location findLocation(Room room);

}
