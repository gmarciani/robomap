package robomap.database;

import java.util.List;

import robomap.model.base.Location;
import robomap.model.home.Home;

public interface HomeDAO {
	
	public void saveHome(Home home);
	
	public void updateHome(Home home);
	
	public void deleteHome(Home home);

	public Home findHome(String homeName);

	public Location getStartLocation(Home home);

	public List<Home> getAll();

}
