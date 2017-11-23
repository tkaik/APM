package com.cognifide.cq.cqsm.graph.data;

import java.util.Set;

import com.google.common.collect.Sets;

public class Graph {
	private Set<Node> nodes = Sets.newHashSet();
	private Set<Edge> edges = Sets.newHashSet();

	public void addEdge(Node from, Node to) {
		nodes.add(from);
		nodes.add(to);
		edges.add(new Edge(from, to));
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public Set<Edge> getEdges() {
		return edges;
	}
}
