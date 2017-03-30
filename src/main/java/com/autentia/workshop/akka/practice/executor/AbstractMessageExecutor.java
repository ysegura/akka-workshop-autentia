package com.autentia.workshop.akka.practice.executor;

import akka.actor.ActorSystem;

class AbstractMessageExecutor {

	private final ActorSystem actorSystem;
	
	public AbstractMessageExecutor(final ActorSystem actorSystem){
		this.actorSystem = actorSystem;
	}
	
	public ActorSystem getActorSystem() {
		return actorSystem;
	}
}
