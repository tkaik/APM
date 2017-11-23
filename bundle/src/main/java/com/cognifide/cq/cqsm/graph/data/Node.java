package com.cognifide.cq.cqsm.graph.data;


import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Node {
	private String id;

	@SerializedName("label")
	private String name;

	private String group = "usergroups";

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

		Node other = (Node) obj;
		return new EqualsBuilder()
				.append(this.id, other.getId())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.hashCode();
	}
}
