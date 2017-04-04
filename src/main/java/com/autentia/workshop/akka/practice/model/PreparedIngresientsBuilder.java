package com.autentia.workshop.akka.practice.model;


import com.autentia.workshop.tortilla.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by blazaro on 2/4/17.
 */
public class PreparedIngresientsBuilder {
    private final TortillaOrder tortillaOrder;
    private final KitchenService kitchenService;
    private HotOliveOil hotOliveOil;
    private BeatenEggs beatenEggs;
    private SlicedPotatoes slicedPotatoes;
    private SlicedOnions slicedOnions;
    private Salt salt;


    public PreparedIngresientsBuilder(TortillaOrder tortillaOrder, KitchenService kitchenService) {
        this.tortillaOrder = tortillaOrder;
        this.kitchenService = kitchenService;
    }

    private PreparedIngresientsBuilder withHotOliveOil() {
        this.hotOliveOil = kitchenService.heatOil(tortillaOrder.getOliveOil());
        return this;
    }

    private PreparedIngresientsBuilder withBeatenEggs() {
        this.beatenEggs = kitchenService.beat(tortillaOrder.getEggs());
        return this;
    }

    private PreparedIngresientsBuilder withSlicedPotatoes() {
        this.slicedPotatoes = kitchenService.slice(tortillaOrder.getPotatoes());
        return this;
    }

    private PreparedIngresientsBuilder withSlicedOnions() {
        this.slicedOnions = kitchenService.slice(tortillaOrder.getOnions());
        return this;
    }

    private PreparedIngresientsBuilder withSalt() {
        this.salt = tortillaOrder.getSalt();
        return this;
    }



    public PreparedIngredients build() {

        this.withBeatenEggs().withHotOliveOil()
                .withSalt().withSlicedPotatoes();
        if (TortillaType.CON_CEBOLLA.equals(tortillaOrder.getTortillaType())) {
            this.withSlicedOnions();
        }


        return  new PreparedIngredients(this.tortillaOrder.getTortillaType(),this.hotOliveOil,this.beatenEggs,this.slicedPotatoes,this.salt,this.slicedOnions);
    }



}
