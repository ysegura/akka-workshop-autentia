package com.autentia.workshop.akka.practice.actor;

import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.dispatch.ExecutionContexts;
import com.autentia.workshop.akka.practice.model.TortillaOrder;
import com.autentia.workshop.tortilla.*;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by blazaro on 1/4/17.
 */
public class ChefActor extends UntypedActor{

    private final KitchenService kitchenService;

    public ChefActor(final KitchenService kitchenService){
        this.kitchenService = kitchenService;
    }


    @Override
    public void onReceive(Object msg) throws Throwable {
        final TortillaOrder tortillaOrder = (TortillaOrder)msg;

        final HotOliveOil hotOliveOil=kitchenService.heatOil(tortillaOrder.getOliveOil());
        final BeatenEggs beatenEggs=kitchenService.beat(tortillaOrder.getEggs());
        final SlicedOnions slicedOnions=kitchenService.slice(tortillaOrder.getOnions());
        final SlicedPotatoes slicedPotatoes=kitchenService.slice(tortillaOrder.getPotatoes());
        

    }
}
