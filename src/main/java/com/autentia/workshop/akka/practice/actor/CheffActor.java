package com.autentia.workshop.akka.practice.actor;

import akka.actor.ActorRef;
import akka.dispatch.Mapper;
import akka.dispatch.OnSuccess;
import com.autentia.workshop.akka.practice.model.TortillaCooker;
import com.autentia.workshop.akka.practice.model.TortillaOrder;
import com.autentia.workshop.akka.practice.model.TortillaType;
import com.autentia.workshop.tortilla.*;

import akka.actor.UntypedActor;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;

/**
 * Created by blazaro on 1/4/17.
 */
public class CheffActor extends UntypedActor {

    private final KitchenService kitchenService;
    private final ActorRef waiterActor;

    public CheffActor(final KitchenService kitchenService, final ActorRef waiterActor) {
        this.kitchenService = kitchenService;
        this.waiterActor = waiterActor;
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        final TortillaOrder tortillaOrder = (TortillaOrder) msg;



        final Future<Tortilla> tortillaFuture = cook(tortillaOrder);



        tortillaFuture.onSuccess(success(tortilla -> waiterActor.tell(tortilla,getSelf())),getExecutionContext());



    }

    private Future<Tortilla> cook(TortillaOrder tortillaOrder) {



        final TortillaCooker tortillaCooker = new TortillaCooker();

        final List<Future<Object>> futures = new ArrayList<Future<Object>>();

        prepareIngredients(tortillaOrder, tortillaCooker, futures);


        return doCook(tortillaOrder,  tortillaCooker, futures);
    }

    private Future<Tortilla> doCook(TortillaOrder tortillaOrder,  TortillaCooker tortillaCooker, List<Future<Object>> futures) {
        final ExecutionContextExecutor executionContext = getExecutionContext();

        final Future<Iterable<Object>> futuresSequence = sequence(futures, executionContext);

        return futuresSequence.map(map(object -> {
            if (TortillaType.CON_CEBOLLA.equals(tortillaOrder.getTortillaType())) {
                return kitchenService.cook(tortillaCooker.getHotOliveOil(), tortillaCooker.getSlicedPotatoes(), tortillaCooker.getSlicedOnions(), tortillaCooker.getBeatenEggs(),
                        tortillaCooker.getSalt());
            } else {
                return kitchenService.cook(tortillaCooker.getHotOliveOil(), tortillaCooker.getSlicedPotatoes(), tortillaCooker.getBeatenEggs(),
                        tortillaCooker.getSalt());
            }
        }), executionContext);
    }

    private void prepareIngredients(TortillaOrder tortillaOrder,  TortillaCooker tortillaCooker, List<Future<Object>> futures) {
        final ExecutionContextExecutor executionContext = getExecutionContext();

        futures.add(future(() -> {
            return tortillaCooker.withHotOliveOil(kitchenService.heatOil(tortillaOrder.getOliveOil()));
        }, executionContext));
        futures.add(future(() -> {
            return tortillaCooker.withBeatenEggs(kitchenService.beat(tortillaOrder.getEggs()));
        }, executionContext));
        futures.add(future(() -> {
            return tortillaCooker.withSlicedPotatoes(kitchenService.slice(tortillaOrder.getPotatoes()));
        }, executionContext));
        futures.add(future(() -> {
            return tortillaCooker.withSalt(tortillaOrder.getSalt());
        }, executionContext));
        if (TortillaType.CON_CEBOLLA.equals(tortillaOrder.getTortillaType())) {
            futures.add(future(() -> {
                return tortillaCooker.withSlicedOnions(kitchenService.slice(tortillaOrder.getOnions()));
            }, executionContext));
        }
    }

    private OnSuccess<Tortilla> success(final Consumer<Tortilla> consumer) {
        return new OnSuccess<Tortilla>() {
            @Override
            public void onSuccess(Tortilla tortilla) throws Throwable {
                consumer.accept(tortilla);
            }


        };
    }

    private Mapper<Iterable<Object>, Tortilla> map(final Function<Iterable<Object>, Tortilla> mapFunction) {
        return new Mapper<Iterable<Object>, Tortilla>() {
            @Override
            public Tortilla apply(Iterable<Object> parameter) {
                return mapFunction.apply(parameter);
            }
        };
    }


    private ExecutionContextExecutor getExecutionContext() {
        return getContext().dispatcher();
    }
}
