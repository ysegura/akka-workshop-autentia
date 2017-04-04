package com.autentia.workshop.akka.practice.actor;


import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.autentia.workshop.akka.practice.model.TortillaOrderBuilder;
import com.autentia.workshop.akka.practice.model.TortillaOrder;
import com.autentia.workshop.akka.practice.model.TortillaType;
import com.autentia.workshop.tortilla.*;

import akka.actor.UntypedActor;
import org.apache.commons.lang3.time.StopWatch;

public class ShopperActor extends UntypedActor {

    private final LoggingAdapter logger = Logging.getLogger(this);

    private final ShopService shopService;

    private final ActorRef cheffActor;

    public ShopperActor(ShopService shopService, ActorRef cheffActor) {
        this.shopService = shopService;
        this.cheffActor = cheffActor;
    }

    @Override
    public void onReceive(Object message) throws Throwable {


        final StopWatch elapsedTime = new StopWatch();
        elapsedTime.start();


        TortillaOrder tortillaOrder = prepareTortillaOrder(message);

        cheffActor.tell(tortillaOrder, this.getSelf());
        elapsedTime.stop();
        logger.info("Shop finished in {}ms.", elapsedTime.getNanoTime());

    }

    private TortillaOrder prepareTortillaOrder(Object message) {
        final TortillaOrderBuilder tortillaOrderBuilder = new TortillaOrderBuilder();
        tortillaOrderBuilder.withEggs(shopService.buyEggs()).withPotatoes(shopService.buyPotatoes())
                .withOliveOil(shopService.buyOliveOil()).withSalt(shopService.buySalt());


        if (TortillaType.CON_CEBOLLA.equals(message)) {
            tortillaOrderBuilder.withOnions(shopService.buyOnions());
        }


        return  tortillaOrderBuilder.build();
    }




}
