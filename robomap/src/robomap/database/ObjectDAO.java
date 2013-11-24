package robomap.database;

import robomap.model.Object;

public interface ObjectDAO {

	public void saveObject(Object object);
	
	public void updateObject(Object object);
	
	public void deleteObject(Object object);

}
