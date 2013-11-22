package robomap.model.graph;

public class Arc {
	
	private Node source;
	private Node destination;
	private int weight;
	
	public Arc(Node source, Node destination, int weight) {
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

	public int getWeight() {
		return this.weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	

}
