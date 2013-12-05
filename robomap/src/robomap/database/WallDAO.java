package robomap.database;


import robomap.model.home.Wall;
import robomap.model.vector.Location;

public interface WallDAO {
	
	public void saveWall(String homeName, Wall wall);

	public boolean isThereAny(Location locationA, Location locationB);

}
