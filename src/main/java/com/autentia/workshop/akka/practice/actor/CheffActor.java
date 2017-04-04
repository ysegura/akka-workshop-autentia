package com.autentia.workshop.akka.practice.actor;

import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.autentia.workshop.akka.practice.model.PreparedIngredients;
import com.autentia.workshop.akka.practice.model.PreparedIngresientsBuilder;
import com.autentia.workshop.akka.practice.model.TortillaOrder;
import com.autentia.workshop.akka.practice.model.TortillaType;
import com.autentia.workshop.tortilla.*;

import akka.actor.UntypedActor;
import org.apache.commons.lang3.time.StopWatch;


/**
 * Created by blazaro on 1/4/17.
 */
public class CheffActor extends UntypedActor {

    private final LoggingAdapter logger = Logging.getLogger(this);

    private final KitchenService kitchenService;
    private final ActorRef waiterActor;

    public CheffActor(final KitchenService kitchenService, final ActorRef waiterActor) {
        this.kitchenService = kitchenService;
        this.waiterActor = waiterActor;
    }

    @Override
    public void onReceive(Object msg) throws Throwable {

        final StopWatch elapsedTime = new StopWatch();
        elapsedTime.start();

        final TortillaOrder tortillaOrder = (TortillaOrder) msg;


        final Tortilla tortilla = cook(tortillaOrder);


        waiterActor.tell(tortilla, getSelf());

        elapsedTime.stop();
        logger.info("Cook finished in {}ms.", elapsedTime.getNanoTime());

    }

    private Tortilla cook(TortillaOrder tortillaOrder) {


        final PreparedIngresientsBuilder preparedIngresientsBuilder = new PreparedIngresientsBuilder(tortillaOrder, kitchenService);


        return doCook(preparedIngresientsBuilder);
    }

    private Tortilla doCook(PreparedIngresientsBuilder preparedIngredientsBuilder) {


        PreparedIngredients preparedIngredients = preparedIngredientsBuilder.build();


        if (TortillaType.CON_CEBOLLA.equals(preparedIngredients.getTortillaType())) {
            return kitchenService.cook(preparedIngredients.getHotOliveOil(), preparedIngredients.getSlicedPotatoes(), preparedIngredients.getSlicedOnions(), preparedIngredients.getBeatenEggs(),
                    preparedIngredients.getSalt());
        } else {
            return kitchenService.cook(preparedIngredients.getHotOliveOil(), preparedIngredients.getSlicedPotatoes(), preparedIngredients.getBeatenEggs(),
                    preparedIngredients.getSalt());
        }

    }

}
