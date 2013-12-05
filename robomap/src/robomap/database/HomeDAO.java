package robomap.database;

import java.util.List;

import robomap.model.home.Home;

public interface HomeDAO {

	public void saveHome(Home home);

	public List<Home> getAll();

}
