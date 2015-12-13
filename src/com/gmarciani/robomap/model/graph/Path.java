package robomap.model.graph;

import java.io.Serializable;
import java.util.List;

/**
 * @project robomap
 *
 * @package robomap.model.graph
 *
 * @class Path
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class Path implements Serializable {

	private static final long serialVersionUID = 256914740459007959L;
	
	private List<Arc> arcs;
	
	public Path(List<Arc> arcs) {
		this.setArcs(arcs);
	}

	public List<Arc> getArcs() {
		return this.arcs;
	}

	public void setArcs(List<Arc> arcs) {
		this.arcs = arcs;
	}
	
	@Override
	public String toString() {
		return "Path(" + 
				this.getArcs() + ")";
	}

}
