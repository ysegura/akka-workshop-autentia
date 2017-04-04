package com.autentia.workshop.akka.practice.actor;

import akka.actor.ActorRef;
import akka.dispatch.Mapper;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.autentia.workshop.akka.practice.model.PreparedIngresientsBuilder;
import com.autentia.workshop.akka.practice.model.TortillaOrder;
import com.autentia.workshop.akka.practice.model.TortillaType;
import com.autentia.workshop.tortilla.*;

import akka.actor.UntypedActor;
import org.apache.commons.lang3.time.StopWatch;
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

    private final LoggingAdapter logger = Logging.getLogger(this);

    private final KitchenService kitchenService;
    private final ActorRef waiterActor;
    private final ExecutionContextExecutor executionContextExecutor;

    public CheffActor(final KitchenService kitchenService, final ActorRef waiterActor) {
        this.kitchenService = kitchenService;
        this.waiterActor = waiterActor;
        this.executionContextExecutor = getContext().dispatcher();
    }

    @Override
    public void onReceive(Object msg) throws Throwable {

        final StopWatch elapsedTime = new StopWatch();
        elapsedTime.start();

        final TortillaOrder tortillaOrder = (TortillaOrder) msg;


        final Future<Tortilla> tortillaFuture = cook(tortillaOrder);


        tortillaFuture.onSuccess(success(tortilla -> waiterActor.tell(tortilla, getSelf())), executionContextExecutor);

        elapsedTime.stop();
        logger.info("Cook finished in {}ms.", elapsedTime.getNanoTime());

    }

    private Future<Tortilla> cook(TortillaOrder tortillaOrder) {


        final PreparedIngresientsBuilder preparedIngresientsBuilder = new PreparedIngresientsBuilder();

        final List<Future<Object>> futures = new ArrayList<Future<Object>>();

        prepareIngredients(tortillaOrder, preparedIngresientsBuilder, futures);


        return doCook(tortillaOrder, preparedIngresientsBuilder, futures);
    }

    private Future<Tortilla> doCook(TortillaOrder tortillaOrder, PreparedIngresientsBuilder preparedIngresientsBuilder, List<Future<Object>> futures) {

        final Future<Iterable<Object>> futuresSequence = sequence(futures, executionContextExecutor);


        return futuresSequence.flatMap(
                map(object -> {
                    if (TortillaType.CON_CEBOLLA.equals(tortillaOrder.getTortillaType())) {
                        return

                                future(()-> kitchenService.cook(preparedIngresientsBuilder.getHotOliveOil(), preparedIngresientsBuilder.getSlicedPotatoes(), preparedIngresientsBuilder.getSlicedOnions(), preparedIngresientsBuilder.getBeatenEggs(),
                                        preparedIngresientsBuilder.getSalt()),executionContextExecutor);
                    } else {
                        return future(()->kitchenService.cook(preparedIngresientsBuilder.getHotOliveOil(), preparedIngresientsBuilder.getSlicedPotatoes(), preparedIngresientsBuilder.getBeatenEggs(),
                                preparedIngresientsBuilder.getSalt()),executionContextExecutor);
                    }
                }), executionContextExecutor);

    }

    private void prepareIngredients(TortillaOrder tortillaOrder, PreparedIngresientsBuilder preparedIngresientsBuilder, List<Future<Object>> futures) {

        futures.add(future(() -> {
            return preparedIngresientsBuilder.withHotOliveOil(kitchenService.heatOil(tortillaOrder.getOliveOil()));
        }, executionContextExecutor));
        futures.add(future(() -> {
            return preparedIngresientsBuilder.withBeatenEggs(kitchenService.beat(tortillaOrder.getEggs()));
        }, executionContextExecutor));
        futures.add(future(() -> {
            return preparedIngresientsBuilder.withSlicedPotatoes(kitchenService.slice(tortillaOrder.getPotatoes()));
        }, executionContextExecutor));
        futures.add(future(() -> {
            return preparedIngresientsBuilder.withSalt(tortillaOrder.getSalt());
        }, executionContextExecutor));
        if (TortillaType.CON_CEBOLLA.equals(tortillaOrder.getTortillaType())) {
            futures.add(future(() -> {
                return preparedIngresientsBuilder.withSlicedOnions(kitchenService.slice(tortillaOrder.getOnions()));
            }, executionContextExecutor));
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

    private Mapper<Iterable<Object>, Future<Tortilla>> map(final Function<Iterable<Object>, Future<Tortilla>> mapFunction) {
        return new Mapper<Iterable<Object>, Future<Tortilla>>() {
            @Override
            public Future<Tortilla> apply(Iterable<Object> parameter) {
                return mapFunction.apply(parameter);
            }
        };
    }


}
