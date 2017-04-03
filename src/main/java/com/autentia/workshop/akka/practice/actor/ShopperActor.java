package com.autentia.workshop.akka.practice.actor;

import com.autentia.workshop.akka.practice.model.TortillaOrder;
import com.autentia.workshop.akka.practice.model.TortillaOrderBuilder;
import com.autentia.workshop.akka.practice.model.TortillaType;
import com.autentia.workshop.tortilla.ShopService;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class ShopperActor extends UntypedActor {

    private final ShopService shopService;

    private final ActorRef chefActor;

    public ShopperActor(ShopService shopService, ActorRef chefActor) {
        this.shopService = shopService;
        this.chefActor = chefActor;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        final TortillaOrderBuilder tortillaOrderBuilder = new TortillaOrderBuilder();

        shopCommonIngredients(tortillaOrderBuilder);

        if (TortillaType.CON_CEBOLLA.equals(message)) {
            tortillaOrderBuilder.withOnions(shopService.buyOnions());
        }
        TortillaOrder order = tortillaOrderBuilder.build();

        chefActor.tell(order, this.getSelf());
    }

    private void shopCommonIngredients(TortillaOrderBuilder tortillaOrderBuilder) {
        tortillaOrderBuilder.withEggs(shopService.buyEggs());
        tortillaOrderBuilder.withPotatoes(shopService.buyPotatoes());
        tortillaOrderBuilder.withOliveOil(shopService.buyOliveOil());
        tortillaOrderBuilder.withSalt(shopService.buySalt());
    }

}
