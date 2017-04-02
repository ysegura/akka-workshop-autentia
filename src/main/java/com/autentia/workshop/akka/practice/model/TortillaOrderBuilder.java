package com.autentia.workshop.akka.practice.model;

import com.autentia.workshop.tortilla.*;

import java.io.Serializable;

/**
 * Created by blazaro on 1/4/17.
 */
public class TortillaOrderBuilder implements Serializable {


    private Salt salt;
    private Eggs eggs;
    private Onions onions;
    private Potatoes potatoes;
    private OliveOil oliveOil;
    private TortillaType tortillaType = TortillaType.SIN_CEBOLLA;

    public TortillaOrderBuilder withSalt(Salt ingredient) {
        this.salt= ingredient;
        return this;
    }

    public TortillaOrderBuilder withEggs(Eggs ingredient) {
        this.eggs= ingredient;
        return this;
    }

    public TortillaOrderBuilder withOnions(Onions ingredient) {
        this.tortillaType = TortillaType.CON_CEBOLLA;
        this.onions= ingredient;
        return this;
    }

    public TortillaOrderBuilder withPotatoes(Potatoes ingredient) {
        this.potatoes= ingredient;
        return this;
    }

    public TortillaOrderBuilder withOliveOil(OliveOil ingredient) {
        this.oliveOil= ingredient;
        return this;
    }



    public TortillaOrder build(){
        return new TortillaOrder(tortillaType,onions,oliveOil,potatoes,eggs,salt);
    }
}
