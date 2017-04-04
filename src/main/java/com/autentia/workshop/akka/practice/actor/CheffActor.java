package com.autentia.workshop.akka.practice.actor;

import akka.actor.ActorRef;
import akka.dispatch.Mapper;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.autentia.workshop.akka.practice.model.PreparedIngredients;
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


        final PreparedIngresientsBuilder preparedIngresientsBuilder = new PreparedIngresientsBuilder(tortillaOrder,kitchenService,executionContextExecutor);


        return doCook(preparedIngresientsBuilder);
    }

    private Future<Tortilla> doCook(PreparedIngresientsBuilder preparedIngredientsBuilder) {


        final Future<PreparedIngredients> preparedIngredientsFuture = preparedIngredientsBuilder.build();


        return preparedIngredientsFuture.flatMap(
                map(preparedIngredients -> {
                    if (TortillaType.CON_CEBOLLA.equals(preparedIngredients.getTortillaType())) {
                        return

                                future(()-> kitchenService.cook(preparedIngredients.getHotOliveOil(), preparedIngredients.getSlicedPotatoes(), preparedIngredients.getSlicedOnions(), preparedIngredients.getBeatenEggs(),
                                        preparedIngredients.getSalt()),executionContextExecutor);
                    } else {
                        return future(()->kitchenService.cook(preparedIngredients.getHotOliveOil(), preparedIngredients.getSlicedPotatoes(), preparedIngredients.getBeatenEggs(),
                                preparedIngredients.getSalt()),executionContextExecutor);
                    }
                }), executionContextExecutor);

    }

    private OnSuccess<Tortilla> success(final Consumer<Tortilla> consumer) {
        return new OnSuccess<Tortilla>() {
            @Override
            public void onSuccess(Tortilla tortilla) throws Throwable {
                consumer.accept(tortilla);
            }


        };
    }

    private Mapper<PreparedIngredients, Future<Tortilla>> map(final Function<PreparedIngredients, Future<Tortilla>> mapFunction) {
        return new Mapper<PreparedIngredients, Future<Tortilla>>() {
            @Override
            public Future<Tortilla> apply(PreparedIngredients parameter) {
                return mapFunction.apply(parameter);
            }
        };
    }


}
