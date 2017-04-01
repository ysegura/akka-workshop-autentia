package com.autentia.workshop.akka.practice.executor;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.autentia.workshop.akka.practice.actor.ShopperActor;
import com.autentia.workshop.akka.practice.actor.WaiterActor;
import com.autentia.workshop.akka.practice.actor.ChefActor;
import com.autentia.workshop.akka.practice.model.Event;

import akka.actor.ActorSystem;
import com.autentia.workshop.tortilla.KitchenService;
import com.autentia.workshop.tortilla.ShopService;
import com.autentia.workshop.tortilla.WaiterService;

public class AkkaMessageExecutor extends AbstractMessageExecutor implements MessageExecutor{
	
	private final String SHOPPER_ACTOR = "shopperActor";
	private final String CHEFF_ACTOR = "cheffActor";
	private final String WAITER_ACTOR = "waiterActor";
	
	public AkkaMessageExecutor(final ActorSystem actorSystem) {
		super(actorSystem);
		
		actorSystem.actorOf(Props.create(ShopperActor.class, new ShopService()), SHOPPER_ACTOR);
		actorSystem.actorOf(Props.create(ChefActor.class, new KitchenService()), CHEFF_ACTOR);
		actorSystem.actorOf(Props.create(WaiterActor.class, new WaiterService("5.56.60.112", 5762,"nig_team_exchange")), WAITER_ACTOR);
	}

	/**
	 * This method will be invoked with every Event consumed from RabbitMQ
	 */
	public void execute(Event event) {
		this.getActorSystem().actorFor(SHOPPER_ACTOR).tell(event.getRequest().getTortillaType(), ActorRef.noSender());
	}

}
