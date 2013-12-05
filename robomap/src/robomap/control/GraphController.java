package robomap.control;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import robomap.model.graph.Arc;
import robomap.model.graph.Node;
import robomap.model.graph.Path;
import robomap.model.home.Home;
import robomap.model.home.Room;
import robomap.model.home.Wall;
import robomap.model.object.Object;
import robomap.model.vector.Dimension;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;

public class GraphController {
	
	private GraphController() {}

	public static DirectedGraph<Node, Arc> parseGraph(Home home) {
		DirectedGraph<Node, Arc> graph = new DirectedSparseGraph<Node, Arc>();
		Arc arcTable[][][] = getArcTable(home);		
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
		return graph;
	}
	
	public static Path computePath(Home home, Location source, Location destination) {
		DirectedGraph<Node, Arc> graph = parseGraph(home);
		Path path = computePath(graph, source, destination);
		return path;
	}

	private static Arc[][][] getArcTable(Home home) {
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
								|| blockedByWall(home, x, y, xad, yad)
								|| isObject(home, x, y)) continue;
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

	private static boolean blockedByWall(Home home, int x, int y, int xad, int yad) {
		List<Wall> walls = home.getWalls();
		for (Wall wall : walls) {
			Location wallLocation = wall.getLocation();
			Direction wallDirection = wall.getDirection();
			int wallLenght = wall.getLenght();
			int wallX = wallLocation.getX();
			int wallY = wallLocation.getY();	
			
			if (wallDirection == Direction.RIGHT) {
				for (int wx = wallX; wx < wallX + wallLenght; wx ++) {
					if ((x == wx && y == wallY - 1 && yad == wallY && (xad == x || xad == x - 1 || xad == x + 1)) 
							|| (x == wx + 1) && y == wallY - 1 && yad == wallY && xad == wx 
							|| (x == wx - 1) && y == wallY - 1 && yad == wallY && xad == wx
							|| (x == wx && y == wallY && yad == wallY - 1 && (xad == x || xad == x - 1 || xad == x + 1))
							|| (x == wx + 1) && y == wallY && yad == wallY - 1 && xad == wx 
							|| (x == wx - 1) && y == wallY && yad == wallY - 1 && xad == wx) {
						return true;
					}
				}
			} else if (wallDirection == Direction.FORWARD) {
				for (int wy = wallY; wy < wallY + wallLenght; wy ++) {
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
		}
		return false;
	}
	
	private static boolean isObject(Home home, int x, int y) {
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

	private static Path computePath(DirectedGraph<Node, Arc> graph, Location source, Location destination) {
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
