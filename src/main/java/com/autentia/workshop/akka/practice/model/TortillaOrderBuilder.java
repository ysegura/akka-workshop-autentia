package com.autentia.workshop.akka.practice.model;

import com.autentia.workshop.tortilla.*;

import java.io.Serializable;

public class TortillaOrderBuilder implements Serializable{

    private TortillaType tortillaType = TortillaType.SIN_CEBOLLA;
    private Onions onions;
    private OliveOil oliveOil;
    private Potatoes potatoes;
    private Eggs eggs;
    private Salt salt;

    public TortillaOrderBuilder withOnions(Onions onions){
        this.tortillaType = TortillaType.CON_CEBOLLA;
        this.onions = onions;
        return this;
    }

    public TortillaOrderBuilder withOliveOil(OliveOil oliveOil){
        this.oliveOil = oliveOil;
        return this;
    }

    public TortillaOrderBuilder withPotatoes(Potatoes potatoes){
        this.potatoes = potatoes;
        return this;
    }

    public TortillaOrderBuilder withEggs (Eggs eggs){
        this.eggs = eggs;
        return this;
    }

    public TortillaOrderBuilder withSalt(Salt salt){
        this.salt = salt;
        return this;
    }

    public TortillaOrder build(){
        return new TortillaOrder(tortillaType, onions, oliveOil, potatoes, eggs, salt);
    }

}
