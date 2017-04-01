package com.autentia.workshop.akka.practice.executor;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.autentia.workshop.akka.practice.actor.ShopperActor;
import com.autentia.workshop.akka.practice.actor.WaiterActor;
import com.autentia.workshop.akka.practice.actor.CheffActor;
import com.autentia.workshop.akka.practice.model.Event;

import akka.actor.ActorSystem;
import com.autentia.workshop.tortilla.KitchenService;
import com.autentia.workshop.tortilla.ShopService;
import com.autentia.workshop.tortilla.WaiterService;

public class AkkaMessageExecutor extends AbstractMessageExecutor implements MessageExecutor{

    private static final String HOST_NAME = "5.56.60.112";

    private static final int PORT_NUMBER = 5672;

    private static final String EXCHANGE = "nig_team_exchange";

    private final String SHOPPER_ACTOR = "shopperActor";
	private final String CHEFF_ACTOR = "cheffActor";
	private final String WAITER_ACTOR = "waiterActor";

	private final ActorRef shopperActor;
	
	public AkkaMessageExecutor(final ActorSystem actorSystem) {
		super(actorSystem);
		
		shopperActor = actorSystem.actorOf(Props.create(ShopperActor.class, new ShopService()), SHOPPER_ACTOR);
		actorSystem.actorOf(Props.create(CheffActor.class, new KitchenService()), CHEFF_ACTOR);
		actorSystem.actorOf(Props.create(WaiterActor.class, new WaiterService(HOST_NAME, PORT_NUMBER, EXCHANGE)), WAITER_ACTOR);
	}

	/**
	 * This method will be invoked with every Event consumed from RabbitMQ
	 */
	public void execute(Event event) {
		shopperActor.tell(event.getRequest().getTortillaType(), ActorRef.noSender());
	}

}
