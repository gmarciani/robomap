package robomap.model.graph;

import java.util.HashSet;
import java.util.Set;


public class Graph {
	
	private Set<Node> nodes;
	private Set<Arc> arcs;
	
	public Graph(Set<Node> nodes, Set<Arc> arcs) {
		this.setNodes(nodes);
		this.setArcs(arcs);
	}
	
	public Graph() {
		this.setNodes(new HashSet<Node>());
		this.setArcs(new HashSet<Arc>());
	}

	public Set<Node> getNodes() {
		return this.nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public Set<Arc> getArcs() {
		return this.arcs;
	}

	public void setArcs(Set<Arc> arcs) {
		this.arcs = arcs;
	}

}
