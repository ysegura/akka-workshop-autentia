package com.autentia.workshop.akka.practice.executor;

import com.autentia.workshop.akka.practice.model.Event;

public interface MessageExecutor {

	void execute(final Event event);
	
}
