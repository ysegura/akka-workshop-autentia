package com.autentia.workshop.akka.practice.actor;

import com.autentia.workshop.akka.practice.model.TortillaType;
import com.autentia.workshop.tortilla.ShopService;

import akka.actor.UntypedActor;

public class ShopperActor extends UntypedActor {

    private final ShopService shopService;

    private final String CHEF_ACTOR = "";

    public ShopperActor(ShopService shopService) {
        this.shopService = shopService;
    }

    @Override
    public void onReceive(Object message) throws Throwable {

        shopService.buyEggs();
        shopService.buyPotatoes();
        shopService.buyOliveOil();
        shopService.buySalt();
        if (TortillaType.CON_CEBOLLA.equals(message)) {
            shopService.buyOnions();
        }

    }

}
