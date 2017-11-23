package com.cognifide.cq.cqsm.graph.data;

public class Edge {
	private String from;
	private String to;

	public Edge(String from, String to) {
		this.from = from;
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
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
