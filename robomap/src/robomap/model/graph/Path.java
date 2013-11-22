package robomap.model.graph;

import java.util.ArrayList;
import java.util.List;

public class Path {
	
	private List<Arc> arcs;
	
	public Path(List<Arc> arcs) {
		this.setArcs(arcs);
	}
	
	public Path() {
		this.setArcs(new ArrayList<Arc>());
	}

	public List<Arc> getArcs() {
		return this.arcs;
	}

	public void setArcs(List<Arc> arcs) {
		this.arcs = arcs;
	}

}
