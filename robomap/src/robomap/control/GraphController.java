package robomap.control;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import robomap.model.base.Dimension;
import robomap.model.base.Location;
import robomap.model.graph.Arc;
import robomap.model.graph.Node;
import robomap.model.graph.Path;
import robomap.model.home.Home;
import robomap.model.home.Room;
import robomap.model.home.Wall;
import robomap.model.object.Object;

public class GraphController {
	
	private static GraphController graphController;
	
	private GraphController() {
		
	}
	
	public static GraphController getInstance() {
		if(graphController == null) {
			graphController = new GraphController();
		}
		return graphController;
	}

	public DirectedGraph<Node, Arc> parseGraph(Home home) {
		DirectedGraph<Node, Arc> graph = new DirectedSparseGraph<Node, Arc>();
		Arc arcTable[][][] = this.getArcTable(home);		
		Dimension homeDimension = home.getDimension();
		int homeWidth = homeDimension.getWidth();
		int homeHeight = homeDimension.getHeight();
		for (int x = 0; x < homeWidth; x ++) {
			for (int y = 0; y < homeHeight; y ++) {
				for (Arc arc : arcTable[x][y]) {
					graph.addEdge(arc, arc.getSource(), arc.getDestination(), EdgeType.DIRECTED);
				}
			}
		}
		System.out.println(graph.toString());
		return graph;
	}

	private Arc[][][] getArcTable(Home home) {
		Dimension homeDimension = home.getDimension();
		int homeWidth = homeDimension.getWidth();
		int homeHeight = homeDimension.getHeight();
		Arc table[][][] = new Arc[homeWidth][homeHeight][8];
		for (int x = 0; x < homeWidth; x ++) {
			for (int y = 0; y < homeHeight; y ++) {
				List<Arc> listArc = new ArrayList<Arc>();
				Node source = new Node(new Location(x, y));
				for (int xad = x - 1; xad <= x + 1; xad ++) {
					for (int yad = y - 1; yad <= y + 1; yad ++) {
						if ((xad == x && yad == y) || 
								xad < 0 || xad >= homeWidth || yad < 0 || yad >= homeHeight
								|| this.blockedByWall(home, x, y, xad, yad)
								|| this.isObject(home, x, y)) continue;
						Node destination = new Node(new Location(xad, yad));
						float cost = (xad != x && yad != y) ? (float) Math.sqrt(2) : 1;
						listArc.add(new Arc(source, destination, cost));
					}					
				}
				table[x][y] = listArc.toArray(new Arc[listArc.size()]);
			}
		}
		return table;
	}

	private boolean blockedByWall(Home home, int x, int y, int xad, int yad) {
		List<Wall> walls = home.getWalls();
		for (Wall wall : walls) {
			Location wallLocation = wall.getLocation();
			Dimension wallDimension = wall.getDimension();
			int wallX = wallLocation.getX();
			int wallY = wallLocation.getY();							
			int wallW = wallDimension.getWidth();
			int wallH = wallDimension.getHeight();
			for (int wx = wallX; wx < wallX + wallW; wx ++) {
				if ((x == wx && y == wallY - 1 && yad == wallY && (xad == x || xad == x - 1 || xad == x + 1)) 
						|| (x == wx + 1) && y == wallY - 1 && yad == wallY && xad == wx 
						|| (x == wx - 1) && y == wallY - 1 && yad == wallY && xad == wx
						|| (x == wx && y == wallY && yad == wallY - 1 && (xad == x || xad == x - 1 || xad == x + 1))
						|| (x == wx + 1) && y == wallY && yad == wallY - 1 && xad == wx 
						|| (x == wx - 1) && y == wallY && yad == wallY - 1 && xad == wx) {
					return true;
				}
			}
			
			for (int wy = wallY; wy < wallY + wallH; wy ++) {
				if ((y == wy && x == wallX - 1 && xad == wallX && (yad == y || yad == y - 1 || yad == y + 1)) 
						|| (y == wy + 1) && x == wallX - 1 && xad == wallX && yad == wy 
						|| (y == wy - 1) && x == wallX - 1 && xad == wallX && yad == wy
						|| (y == wy && x == wallX && xad == wallX - 1 && (yad == y || yad == y - 1 || yad == y + 1))
						|| (y == wy + 1) && x == wallX && xad == wallX - 1 && yad == wy 
						|| (y == wy - 1) && x == wallX && xad == wallX - 1 && yad == wy) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isObject(Home home, int x, int y) {
		List<Object> objects = new ArrayList<Object>();
		for (Room room : home.getRooms()) {
			objects.addAll(room.getObjects());
		}
		for (Object object : objects) {
			Location objectLocation = object.getLocation();
			Dimension objectDimension = object.getDimension();
			int objectX = objectLocation.getX();
			int objectY = objectLocation.getY();							
			int objectW = objectDimension.getWidth();
			int objectH = objectDimension.getHeight();
			for (int ox = objectX; ox < objectX + objectW; ox ++) {
				for (int oy = objectY; oy < objectY + objectH; oy ++) {
					if (x == ox && y == oy) return true;
				}
			}
		}
		return false;
	}

	public Path computePath(DirectedGraph<Node, Arc> graph, Location source, Location destination) {
		Transformer<Arc, Float> transformer = new Transformer<Arc, Float>() {
			public Float transform(Arc arc) {
				return arc.getWeight();
			}
		};
		DijkstraShortestPath<Node, Arc> alg = new DijkstraShortestPath<Node, Arc>(graph, transformer);		
		List<Arc> listArc = alg.getPath(new Node(source), new Node(destination));
		return new Path(listArc);
	}
	
}
