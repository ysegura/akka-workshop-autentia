package com.autentia.workshop.akka.practice.actor;

import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;

import akka.actor.ActorRef;
import akka.dispatch.Mapper;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.autentia.workshop.akka.practice.model.TortillaOrderBuilder;
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

public class ShopperActor extends UntypedActor {

    private final LoggingAdapter logger = Logging.getLogger(this);

    private final ShopService shopService;

    private final ActorRef cheffActor;
    private final ExecutionContextExecutor executionContextExecutor;

    public ShopperActor(ShopService shopService, ActorRef cheffActor) {
        this.shopService = shopService;
        this.cheffActor = cheffActor;
        this.executionContextExecutor = getContext().dispatcher();
    }

    @Override
    public void onReceive(Object message) throws Throwable {


        final StopWatch elapsedTime = new StopWatch();
        elapsedTime.start();
        final TortillaOrderBuilder tortillaOrderBuilder = new TortillaOrderBuilder();

        final Future<TortillaOrder> futureTortillaOrder = prepareTortillaOrder(message, tortillaOrderBuilder);

        futureTortillaOrder.onSuccess(success(tortillaOrder -> {
                    cheffActor.tell(tortillaOrder, this.getSelf());
                })
                , executionContextExecutor);
        elapsedTime.stop();
        logger.info("Shop finished in {}ms.",elapsedTime.getNanoTime());

    }

    private Future<TortillaOrder> prepareTortillaOrder(Object message, TortillaOrderBuilder tortillaOrderBuilder) {
        final List<Future<Object>> futures = doShop(message, tortillaOrderBuilder);

        final Future<Iterable<Object>> futuresSequence = sequence(futures, executionContextExecutor);

        return futuresSequence.map(
                map(iterableObject -> tortillaOrderBuilder.build()), executionContextExecutor);
    }

    private List<Future<Object>> doShop(Object message, TortillaOrderBuilder tortillaOrderBuilder) {
        final List<Future<Object>> futures = new ArrayList<Future<Object>>();
        futures.add(future(() -> {
            return tortillaOrderBuilder.withEggs(shopService.buyEggs());
        }, executionContextExecutor));
        futures.add(future(() -> {
            return tortillaOrderBuilder.withPotatoes(shopService.buyPotatoes());
        }, executionContextExecutor));
        futures.add(future(() -> {
            return tortillaOrderBuilder.withOliveOil(shopService.buyOliveOil());
        }, executionContextExecutor));
        futures.add(future(() -> {
            return tortillaOrderBuilder.withSalt(shopService.buySalt());
        }, executionContextExecutor));


        if (TortillaType.CON_CEBOLLA.equals(message)) {
            futures.add(future(() -> {
                return tortillaOrderBuilder.withOnions(shopService.buyOnions());
            }, executionContextExecutor));
        }
        return futures;
    }

    private Mapper<Iterable<Object>, TortillaOrder> map(final Function<Iterable<Object>, TortillaOrder> mapFunction) {
        return new Mapper<Iterable<Object>, TortillaOrder>() {
            @Override
            public TortillaOrder apply(Iterable<Object> parameter) {
                return mapFunction.apply(parameter);
            }
        };
    }

    private OnSuccess<TortillaOrder> success(final Consumer<TortillaOrder> consumer) {
        return new OnSuccess<TortillaOrder>() {
            @Override
            public void onSuccess(TortillaOrder tortillaOrder) throws Throwable {
                consumer.accept(tortillaOrder);
            }


        };
    }




}
