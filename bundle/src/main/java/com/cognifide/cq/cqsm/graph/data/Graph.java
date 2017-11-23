package com.cognifide.cq.cqsm.graph.data;

import java.util.List;

public class Graph {
	List<Node> nodes;
	List<Edge> edges;

	public Graph(List<Node> nodes, List<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}
}
