package robomap.control;

import robomap.model.Home;
import robomap.model.Location;
import robomap.model.graph.Graph;
import robomap.model.graph.Path;

public class GraphController {
	
	private static GraphController graphController = null;
	
	private GraphController() {
		
	}
	
	public static GraphController getInstance() {
		if(graphController == null) {
			graphController = new GraphController();
		}
		return graphController;
	}

	public Graph parseGraph(Home home) {
		// TODO Auto-generated method stub
		return null;
	}

	public Path computePath(Location source, Location destination) {
		// TODO Auto-generated method stub
		return null;
	}

}
