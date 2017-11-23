package com.cognifide.cq.cqsm.graph.data;


import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Node {
	private String id;

	@SerializedName("label")
	private String name;

	private String group;

	public Node(String id, String name) {
		this(id, name, "usergroups");
	}

	public Node(String id, String name, String group) {
		this.id = id;
		this.name = name;
		this.group = group;
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

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
