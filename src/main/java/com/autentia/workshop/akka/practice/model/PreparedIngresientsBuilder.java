package com.autentia.workshop.akka.practice.model;


import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;

import akka.dispatch.Mapper;
import akka.dispatch.OnSuccess;
import com.autentia.workshop.tortilla.*;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by blazaro on 2/4/17.
 */
public class PreparedIngresientsBuilder {
    private final TortillaOrder tortillaOrder;
    private final KitchenService kitchenService;
    private final ExecutionContextExecutor executionContextExecutor;
    private final List<Future<Object>> preparedIngredients = new ArrayList<>();
    private HotOliveOil hotOliveOil;
    private BeatenEggs beatenEggs;
    private SlicedPotatoes slicedPotatoes;
    private SlicedOnions slicedOnions;
    private Salt salt;


    public PreparedIngresientsBuilder(TortillaOrder tortillaOrder, KitchenService kitchenService, ExecutionContextExecutor executionContextExecutor) {
        this.tortillaOrder = tortillaOrder;
        this.kitchenService = kitchenService;
        this.executionContextExecutor = executionContextExecutor;
    }

    private PreparedIngresientsBuilder withHotOliveOil() {
        final Future<Object> future = future(() -> {
            return kitchenService.heatOil(tortillaOrder.getOliveOil());
        }, executionContextExecutor);
        preparedIngredients.add(future);
        future.onSuccess(success(hotOliveOil -> this.hotOliveOil = (HotOliveOil) hotOliveOil), executionContextExecutor);
        return this;
    }

    private PreparedIngresientsBuilder withBeatenEggs() {
        final Future<Object> future = future(() -> {
            return kitchenService.beat(tortillaOrder.getEggs());
        }, executionContextExecutor);
        preparedIngredients.add(future);
        future.onSuccess(success(beatenEggs -> this.beatenEggs = (BeatenEggs) beatenEggs), executionContextExecutor);
        return this;
    }

    private PreparedIngresientsBuilder withSlicedPotatoes() {
        final Future<Object> future = future(() -> {
            return kitchenService.slice(tortillaOrder.getPotatoes());
        }, executionContextExecutor);
        preparedIngredients.add(future);
        future.onSuccess(success(slicedPotatoes -> this.slicedPotatoes = (SlicedPotatoes) slicedPotatoes), executionContextExecutor);
        return this;
    }

    private PreparedIngresientsBuilder withSlicedOnions() {
        final Future<Object> future = future(() -> {
            return kitchenService.slice(tortillaOrder.getOnions());
        }, executionContextExecutor);
        preparedIngredients.add(future);
        future.onSuccess(success(slicedOnions -> this.slicedOnions = (SlicedOnions) slicedOnions), executionContextExecutor);
        return this;
    }

    private PreparedIngresientsBuilder withSalt() {
        this.salt = tortillaOrder.getSalt();
        return this;
    }

    private OnSuccess<Object> success(final Consumer<Object> consumer) {
        return new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object preparedIngredient) throws Throwable {
                consumer.accept(preparedIngredient);
            }


        };
    }


    public Future<PreparedIngredients> build() {

        this.withBeatenEggs().withHotOliveOil()
                .withSalt().withSlicedPotatoes();
        if (TortillaType.CON_CEBOLLA.equals(tortillaOrder.getTortillaType())) {
            this.withSlicedOnions();
        }


        Future<Iterable<Object>> futuresSequence= sequence(preparedIngredients, executionContextExecutor);

        return futuresSequence.map(
                map(object -> {
                    return new PreparedIngredients(this.tortillaOrder.getTortillaType(),this.hotOliveOil,this.beatenEggs,this.slicedPotatoes,this.salt,this.slicedOnions);
                }), executionContextExecutor);
    }

    private Mapper<Iterable<Object>, PreparedIngredients> map(final Function<Iterable<Object>, PreparedIngredients> mapFunction) {
        return new Mapper<Iterable<Object>, PreparedIngredients>() {
            @Override
            public PreparedIngredients apply(Iterable<Object> parameter) {
                return mapFunction.apply(parameter);
            }
        };
    }


}
