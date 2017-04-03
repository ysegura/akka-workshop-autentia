package com.autentia.workshop.akka.practice.actor;

import akka.actor.Actor;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.autentia.workshop.tortilla.Tortilla;
import com.autentia.workshop.tortilla.WaiterService;

/**
 * Created by blazaro on 1/4/17.
 */
public class WaiterActor extends UntypedActor {

    private final WaiterService waiterService;
    private final LoggingAdapter loggingAdapter = Logging.getLogger(this);

    private int tortillasServed = 0;

    public WaiterActor (WaiterService waiterService){

        this.waiterService = waiterService;
    }
    @Override
    public void onReceive(Object o) throws Throwable {
        waiterService.serveTortilla((Tortilla) o);
        tortillasServed++;
        loggingAdapter.info("Servida tortilla: " + tortillasServed);
    }
}
