package com.autentia.workshop.akka.practice.actor;

import com.autentia.workshop.akka.practice.model.TortillaOrder;
import com.autentia.workshop.tortilla.*;

import akka.actor.UntypedActor;

/**
 * Created by blazaro on 1/4/17.
 */
public class CheffActor extends UntypedActor {

    private final KitchenService kitchenService;

    public CheffActor(final KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        final TortillaOrder tortillaOrder = (TortillaOrder)msg;
        if (tortillaOrder.getOnions() != null) {
            final HotOliveOil hotOliveOil = kitchenService.heatOil(tortillaOrder.getOliveOil());
            final BeatenEggs beatenEggs = kitchenService.beat(tortillaOrder.getEggs());
            final SlicedOnions slicedOnions = kitchenService.slice(tortillaOrder.getOnions());
            final SlicedPotatoes slicedPotatoes = kitchenService.slice(tortillaOrder.getPotatoes());

            final Tortilla tortilla = kitchenService.cook(hotOliveOil, slicedPotatoes, slicedOnions, beatenEggs,
                    tortillaOrder.getSalt());
            this.getContext().actorSelection("/user/waiterActor").tell(tortilla, getSelf());
        } else {
            final HotOliveOil hotOliveOil = kitchenService.heatOil(tortillaOrder.getOliveOil());
            final BeatenEggs beatenEggs = kitchenService.beat(tortillaOrder.getEggs());
            final SlicedPotatoes slicedPotatoes = kitchenService.slice(tortillaOrder.getPotatoes());

            final Tortilla tortilla = kitchenService.cook(hotOliveOil, slicedPotatoes, beatenEggs,
                    tortillaOrder.getSalt());
            this.getContext().actorSelection("/user/waiterActor").tell(tortilla, getSelf());
        }

    }
}
