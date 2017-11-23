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
}
