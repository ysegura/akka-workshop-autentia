package com.autentia.workshop.akka.practice.actor;

import akka.actor.ActorSystem;
import com.autentia.workshop.akka.practice.model.TortillaOrder;
import com.autentia.workshop.akka.practice.model.TortillaType;
import com.autentia.workshop.tortilla.*;

import akka.actor.UntypedActor;

public class ShopperActor extends UntypedActor {

    private final ShopService shopService;

    private final String CHEFF_ACTOR = "cheffActor";

    public ShopperActor(ShopService shopService) {
        this.shopService = shopService;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        TortillaType type = TortillaType.SIN_CEBOLLA;
        Eggs eggs = shopService.buyEggs();
        Potatoes potatoes = shopService.buyPotatoes();
        OliveOil oliveOil = shopService.buyOliveOil();
        Salt salt = shopService.buySalt();
        Onions onions = null;
        if (TortillaType.CON_CEBOLLA.equals(message)) {
            type = TortillaType.CON_CEBOLLA;
            onions = shopService.buyOnions();
        }
        TortillaOrder order = new TortillaOrder(type,onions, oliveOil, potatoes, eggs, salt);

        this.sender().tell(order, this.getSelf());

    }

}
