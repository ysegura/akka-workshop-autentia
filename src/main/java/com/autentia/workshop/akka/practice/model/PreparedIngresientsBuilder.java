package com.autentia.workshop.akka.practice.model;

import com.autentia.workshop.tortilla.*;

/**
 * Created by blazaro on 2/4/17.
 */
public class PreparedIngresientsBuilder {
    private HotOliveOil hotOliveOil;
    private BeatenEggs beatenEggs;
    private SlicedPotatoes slicedPotatoes;
    private SlicedOnions slicedOnions;
    private Salt salt;

    public PreparedIngresientsBuilder withHotOliveOil(HotOliveOil hotOliveOil) {
        this.hotOliveOil = hotOliveOil;
        return this;
    }

    public PreparedIngresientsBuilder withBeatenEggs(BeatenEggs beat) {
        beatenEggs = beat;
        return this;
    }

    public PreparedIngresientsBuilder withSlicedPotatoes(SlicedPotatoes slicedPotatoes) {
        this.slicedPotatoes = slicedPotatoes;
        return this;
    }

    public PreparedIngresientsBuilder withSlicedOnions(SlicedOnions slicedOnions) {
        this.slicedOnions = slicedOnions;
        return this;
    }

    public PreparedIngresientsBuilder withSalt(Salt salt) {
        this.salt = salt;
        return this;
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

    public SlicedOnions getSlicedOnions() {
        return slicedOnions;
    }

    public Salt getSalt() {
        return salt;
    }
}
