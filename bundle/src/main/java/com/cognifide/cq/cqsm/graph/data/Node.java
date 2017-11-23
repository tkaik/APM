package com.cognifide.cq.cqsm.graph.data;


public class Node {
	private String id;
	private String name;

	public Node(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
