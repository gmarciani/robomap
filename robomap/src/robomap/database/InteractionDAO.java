package robomap.database;

import robomap.model.object.Interaction;

/**
 * @project robomap
 *
 * @package robomap.database
 *
 * @class InteractionDAO
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public interface InteractionDAO {
	
	public void saveInteraction(Interaction interaction);

	public void saveInteraction(String objectName, Interaction interaction);	

}
