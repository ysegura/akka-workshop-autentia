package com.autentia.workshop.akka.practice.model;

import java.io.Serializable;

import com.autentia.workshop.tortilla.*;

public class TortillaCooker implements Serializable {

    private final KitchenService kitchenService;

    private HotOliveOil hotOliveOil;

    private BeatenEggs beatenEggs;

    private SlicedPotatoes slicedPotatoes;

    private SlicedOnions slicedOnions;

    private Salt salt;

    private boolean withOnions = false;

    public TortillaCooker(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    public TortillaCooker withHotOliveOil(OliveOil oliveOil) {
        this.hotOliveOil = kitchenService.heatOil(oliveOil);
        return this;
    }

    public TortillaCooker withBeatenEggs(Eggs eggs) {
        this.beatenEggs = kitchenService.beat(eggs);
        return this;
    }

    public TortillaCooker withSlicedPotatoes(Potatoes potatoes) {
        this.slicedPotatoes = kitchenService.slice(potatoes);
        return this;
    }

    public TortillaCooker withSlicedOnions(Onions onions) {
        withOnions = true;
        this.slicedOnions = kitchenService.slice(onions);
        return this;
    }

    public TortillaCooker withSalt(Salt salt) {
        this.salt = salt;
        return this;
    }

    public Tortilla cook() {
        if (withOnions) {
            return kitchenService.cook(this.hotOliveOil, this.slicedPotatoes, this.slicedOnions, this.beatenEggs,
                    this.salt);
        } else {
            return kitchenService.cook(this.hotOliveOil, this.slicedPotatoes, this.beatenEggs,
                    this.salt);
        }
    }
}