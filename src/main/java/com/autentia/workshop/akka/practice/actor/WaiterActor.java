package com.autentia.workshop.akka.practice.actor;

import akka.actor.Actor;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.autentia.workshop.tortilla.Tortilla;
import com.autentia.workshop.tortilla.WaiterService;
import org.apache.commons.lang3.time.StopWatch;

import static akka.dispatch.Futures.future;

/**
 * Created by blazaro on 1/4/17.
 */
public class WaiterActor extends UntypedActor {

    private final LoggingAdapter logger = Logging.getLogger(this);

    private final WaiterService waiterService;
    private final LoggingAdapter loggingAdapter = Logging.getLogger(this);

    public WaiterActor (WaiterService waiterService){

        this.waiterService = waiterService;
    }
    @Override
    public void onReceive(Object o) throws Throwable {

        final StopWatch elapsedTime = new StopWatch();
        elapsedTime.start();


        future(() -> {
             waiterService.serveTortilla((Tortilla) o);
             return 0;
        }, this.getContext().dispatcher())
        ;

        elapsedTime.stop();
        logger.info("Waiter finished in {}ms.",elapsedTime.getNanoTime());

    }
}
