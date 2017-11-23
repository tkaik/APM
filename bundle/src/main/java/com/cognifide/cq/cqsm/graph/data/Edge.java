package com.cognifide.cq.cqsm.graph.data;

import com.google.gson.annotations.SerializedName;

public class Edge {
	@SerializedName("fromNode")
	private Node from;

	@SerializedName("toNode")
	private Node to;

	@SerializedName("from")
	String fromId;

	@SerializedName("to")
	String toId;

	public Edge(Node from, Node to) {
		this.from = from;
		this.fromId = from.getId();
		this.to = to;
		this.toId = to.getId();
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
