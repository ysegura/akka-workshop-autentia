package com.autentia.workshop.akka.practice.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autentia.workshop.messagebroker.Event;
import com.autentia.workshop.messagebroker.MessageExecutor;

import akka.actor.ActorSystem;

public class FakeMessageExecutor extends AbstractMessageExecutor implements MessageExecutor{
	
	public FakeMessageExecutor(ActorSystem actorSystem) {
		super(actorSystem);
	}

	private final Logger log = LoggerFactory.getLogger(getClass());

	public void execute(Event event) {
		log.info("Just received!!! Event: "+event.getId());		
	}

}
