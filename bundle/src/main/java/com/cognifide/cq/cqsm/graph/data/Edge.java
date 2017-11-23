package com.cognifide.cq.cqsm.graph.data;

public class Edge {
	private Node from;
	private Node to;

	public Edge(Node from, Node to) {
		this.from = from;
		this.to = to;
	}

	public Node getFrom() {
		return from;
	}

	public Node getTo() {
		return to;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Edge)) {
			return false;
		}
		Edge edgeObj = (Edge)obj;
		return this.getFrom().equals(edgeObj.getFrom()) && this.getTo().equals(edgeObj.getTo());
	}
}
