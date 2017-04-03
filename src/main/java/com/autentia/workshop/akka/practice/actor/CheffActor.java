package com.autentia.workshop.akka.practice.actor;

import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.autentia.workshop.akka.practice.model.TortillaCooker;
import com.autentia.workshop.akka.practice.model.TortillaOrder;
import com.autentia.workshop.akka.practice.model.TortillaType;
import com.autentia.workshop.tortilla.KitchenService;
import com.autentia.workshop.tortilla.Tortilla;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.dispatch.Mapper;
import akka.dispatch.OnSuccess;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.Future;

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
        final TortillaOrder tortillaOrder = (TortillaOrder)msg;

        final Future<Tortilla> tortillaFuture = cook(tortillaOrder);

        tortillaFuture.onSuccess(success(tortilla -> waiterActor.tell(tortilla, getSelf())), getExecutionContext());

    }

    private Future<Tortilla> cook(TortillaOrder tortillaOrder) {

        TortillaCooker tortillaCooker = new TortillaCooker(kitchenService);
        final List<Future<Object>> ingredientsFuture = prepareIngredients(tortillaOrder, tortillaCooker);

        return doCook(tortillaOrder, tortillaCooker, ingredientsFuture);

    }

    private OnSuccess<Tortilla> success(final Consumer<Tortilla> consumer) {
        return new OnSuccess<Tortilla>() {

            @Override
            public void onSuccess(Tortilla tortilla) throws Throwable {
                consumer.accept(tortilla);
            }
        };
    }

    private ExecutionContextExecutor getExecutionContext() {
        return getContext().dispatcher();
    }

    private List<Future<Object>> prepareIngredients(TortillaOrder order, TortillaCooker cooker) {
        ExecutionContextExecutor executor = getExecutionContext();
        List<Future<Object>> futures = new ArrayList<Future<Object>>();

        futures.add(future(() -> cooker.withBeatenEggs(order.getEggs()), executor));
        futures.add(future(() -> cooker.withSalt(order.getSalt()), executor));
        futures.add(future(() -> cooker.withHotOliveOil(order.getOliveOil()), executor));
        futures.add(future(() -> cooker.withSlicedPotatoes(order.getPotatoes()), executor));
        if (TortillaType.CON_CEBOLLA.equals(order.getTortillaType())) {
            futures.add(future(() -> cooker.withSlicedOnions(order.getOnions()), executor));
        }
        return futures;
    }

    private Future<Tortilla> doCook(TortillaOrder order, TortillaCooker cooker,
            List<Future<Object>> ingredientsFuture) {
        final ExecutionContextExecutor executionContext = getExecutionContext();

        final Future<Iterable<Object>> futuresSequence = sequence(ingredientsFuture, executionContext);

        return futuresSequence.map(map(object -> cooker.cook()), executionContext);

    }

    private Mapper<Iterable<Object>, Tortilla> map(final Function<Iterable<Object>, Tortilla> mapFunction) {
        return new Mapper<Iterable<Object>, Tortilla>() {

            @Override
            public Tortilla apply(Iterable<Object> parameter) {
                return mapFunction.apply(parameter);
            }
        };
    }

}
