package com.cognifide.cq.cqsm.graph.data;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
		Edge other = (Edge)obj;
		return new EqualsBuilder()
				.append(from, other.getFrom())
				.append(to, other.getTo())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(from)
				.append(to)
				.hashCode();
	}
}
