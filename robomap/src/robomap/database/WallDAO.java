package robomap.database;

import robomap.model.Wall;

public interface WallDAO {
	
	public void saveWall(String homeName, Wall wall);
	
	public void updateWall(String homeName, Wall wall);
	
	public void deleteWall(String homeName, Wall wall);

}
