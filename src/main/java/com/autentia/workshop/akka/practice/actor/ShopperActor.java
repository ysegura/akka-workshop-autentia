package com.autentia.workshop.akka.practice.actor;

import com.autentia.workshop.tortilla.*;

import akka.actor.UntypedActor;


public class ShopperActor extends UntypedActor {

    private final ShopService shopService;

    public ShopperActor(ShopService shopService) {
        this.shopService = shopService;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        shopService.buyEggs();
        shopService.buyPotatoes();
        shopService.buyOliveOil();
        shopService.buySalt();
        if (message instanceof TortillaConCebolla){
            shopService.buyOnions();
        }

    }

}
