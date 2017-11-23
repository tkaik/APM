package com.cognifide.cq.cqsm.graph.data;


import com.google.gson.annotations.SerializedName;

public class Node {
	private String id;

	@SerializedName("label")
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

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Node)) {
			return false;
		}
		return this.getId().equals(((Node)obj).getId());
	}
}
