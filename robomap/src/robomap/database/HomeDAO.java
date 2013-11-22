package robomap.database;

import robomap.model.Home;

public interface HomeDAO {

	Home findHome(String homeName);

}
