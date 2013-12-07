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
import robomap.model.home.Wall;
import robomap.model.object.Object;
import robomap.model.vector.Dimension;
import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.control
 *
 * @class GraphController
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public abstract class GraphController {
	
	public static Path computePath(Home home, Location source, Location destination) {
		DirectedGraph<Node, Arc> graph = parseGraph(home);
		Path path = computePath(graph, source, destination);
		return path;
	}
	
	public static Path computeSelectivePath(Home home, Location source, Location destination, Location withoutLocation) {
		DirectedGraph<Node, Arc> graph = parseGraph(home);
		graph.removeVertex(new Node(withoutLocation));
		Path path = computePath(graph, source, destination);
		return path;
	}		
	
	public static Path computeSelectivePath(Home home, Location source, Location destination, List<Location> withoutLocations) {
		DirectedGraph<Node, Arc> graph = parseGraph(home);
		for (Location location : withoutLocations) {
			graph.removeVertex(new Node(location));
		}
		Path path = computePath(graph, source, destination);
		return path;
	}

	private static DirectedGraph<Node, Arc> parseGraph(Home home) {
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
	
	private static Path computePath(DirectedGraph<Node, Arc> graph, Location source, Location destination) {
		if (source.equals(destination)) {
			List<Arc> listArc = new ArrayList<Arc>();
			Arc arc = new Arc(new Node(source), new Node(destination), 0);
			listArc.add(arc);
			return new Path(listArc);
		}
		
		Transformer<Arc, Float> transformer = new Transformer<Arc, Float>() {
			public Float transform(Arc arc) {
				return arc.getWeight();
			}
		};
		DijkstraShortestPath<Node, Arc> alg = new DijkstraShortestPath<Node, Arc>(graph, transformer);		
		List<Arc> listArc = alg.getPath(new Node(source), new Node(destination));
		return new Path(listArc);
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
		for (Wall wall : home.getWalls()) {
			if (wall.blocks(new Location(x, y), new Location(xad, yad))) {
				return true;		
			}
		}
		return false;
	}
	
	private static boolean isObject(Home home, int x, int y) {
		for (Object object : home.getObjects()) {
			if (object.comprehend(new Location(x, y))) {
				return true;
			}
		}
		return false;
	}

	
}
