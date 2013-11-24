package robomap.database;

import robomap.model.Home;

public interface HomeDAO {
	
	public void saveHome(Home home);
	
	public void updateHome(Home home);
	
	public void deleteHome(Home home);

	public Home findHome(String homeName);

}
