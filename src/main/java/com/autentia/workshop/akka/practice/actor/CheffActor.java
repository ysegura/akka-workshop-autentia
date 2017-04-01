package com.autentia.workshop.akka.practice.actor;

import akka.actor.ActorRef;
import com.autentia.workshop.akka.practice.model.TortillaOrder;
import com.autentia.workshop.akka.practice.model.TortillaType;
import com.autentia.workshop.tortilla.*;

import akka.actor.UntypedActor;

/**
 * Created by blazaro on 1/4/17.
 */
public class CheffActor extends UntypedActor {

    private final KitchenService kitchenService;
    private final ActorRef waiterActor;

    public CheffActor(final KitchenService kitchenService,  final ActorRef waiterActor) {
        this.kitchenService = kitchenService;
        this.waiterActor = waiterActor;
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        final TortillaOrder tortillaOrder = (TortillaOrder)msg;


        final Tortilla tortilla = cook(tortillaOrder);


        waiterActor.tell(tortilla, getSelf());


    }

    private Tortilla cook(TortillaOrder tortillaOrder) {
        final HotOliveOil hotOliveOil = kitchenService.heatOil(tortillaOrder.getOliveOil());
        final BeatenEggs beatenEggs = kitchenService.beat(tortillaOrder.getEggs());
        final SlicedPotatoes slicedPotatoes = kitchenService.slice(tortillaOrder.getPotatoes());

        if(TortillaType.CON_CEBOLLA.equals(tortillaOrder.getTortillaType())) {
            final SlicedOnions slicedOnions = kitchenService.slice(tortillaOrder.getOnions());
            return kitchenService.cook(hotOliveOil, slicedPotatoes, slicedOnions, beatenEggs,
                    tortillaOrder.getSalt());
        }else{
            return kitchenService.cook(hotOliveOil, slicedPotatoes, beatenEggs,
                    tortillaOrder.getSalt());
        }
    }
}
