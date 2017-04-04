package com.autentia.workshop.akka.practice.model;

import com.autentia.workshop.tortilla.*;

/**
 * Created by blazaro on 4/4/17.
 */
public class PreparedIngredients {
    private final TortillaType tortillaType;
    private final HotOliveOil hotOliveOil;
    private final BeatenEggs beatenEggs;
    private final SlicedPotatoes slicedPotatoes;
    private final Salt salt;
    private final SlicedOnions slicedOnions;

    public PreparedIngredients(TortillaType tortillaType, HotOliveOil hotOliveOil, BeatenEggs beatenEggs, SlicedPotatoes slicedPotatoes, Salt salt, SlicedOnions slicedOnions) {
        this.tortillaType = tortillaType;
        this.hotOliveOil = hotOliveOil;
        this.beatenEggs = beatenEggs;
        this.slicedPotatoes = slicedPotatoes;
        this.salt = salt;
        this.slicedOnions = slicedOnions;
    }

    public TortillaType getTortillaType() {
        return tortillaType;
    }

    public HotOliveOil getHotOliveOil() {
        return hotOliveOil;
    }

    public BeatenEggs getBeatenEggs() {
        return beatenEggs;
    }

    public SlicedPotatoes getSlicedPotatoes() {
        return slicedPotatoes;
    }

    public Salt getSalt() {
        return salt;
    }

    public SlicedOnions getSlicedOnions() {
        return slicedOnions;
    }
}
