package com.autentia.workshop.akka.practice.model;

import com.autentia.workshop.tortilla.*;

import java.io.Serializable;

/**
 * Created by blazaro on 1/4/17.
 */
public class TortillaOrder implements Serializable{

    private final TortillaType tortillaType;
    private final Onions onions;
    private final OliveOil oliveOil;
    private final Potatoes potatoes;
    private final Eggs eggs;
    private final Salt salt;

    public TortillaOrder(TortillaType tortillaType, Onions onions, OliveOil oliveOil, Potatoes potatoes, Eggs eggs, Salt salt){

        this.tortillaType = tortillaType;
        this.onions = onions;
        this.oliveOil = oliveOil;
        this.potatoes = potatoes;
        this.eggs = eggs;
        this.salt = salt;
    }
}
