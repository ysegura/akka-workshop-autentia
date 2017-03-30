package com.autentia.workshop.akka.practice.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Event implements Serializable {

	private final String id;
	private final Payload payload;

	public Event(String id, Payload payload) {
		super();
		this.id = id;
		this.payload = payload;
	}

	public String getId() {
		return id;
	}

	public Payload getPayload() {
		return payload;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(payload).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Event)) {
			return false;
		}

		final Event anotherObj = (Event) obj;
		return new EqualsBuilder().append(id, anotherObj.id).append(payload, anotherObj.payload).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	

}
