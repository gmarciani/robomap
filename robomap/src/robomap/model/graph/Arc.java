package robomap.model.graph;

import java.io.Serializable;

/**
 * @project robomap
 *
 * @package robomap.model.graph
 *
 * @class Arc
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
public class Arc implements Serializable {

	private static final long serialVersionUID = -7749827824424648060L;
	
	private Node source;
	private Node destination;
	private float weight;
	
	public Arc(Node source, Node destination, float weight) {
		this.setSource(source);
		this.setDestination(destination);
		this.setWeight(weight);
	}	

	public Node getSource() {
		return this.source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getDestination() {
		return this.destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}
	
	public float getWeight() {
		return this.weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
	
	@Override
    public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		 
        Arc arc = (Arc) o;
 
        return this.getSource().equals(arc.getSource()) &&
        		this.getDestination().equals(arc.getDestination()) &&
        		this.getWeight() == arc.getWeight();
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + Float.floatToIntBits(weight);
		return result;
	}
	
	@Override
	public String toString() {
		return "Arc(" + 
				this.getSource().toString() + "; " +
				this.getDestination().toString() + "; " +
				this.getWeight() + ")";
	}

}
