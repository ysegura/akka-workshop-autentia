package com.autentia.workshop.akka.practice.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Payload implements Serializable {
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
