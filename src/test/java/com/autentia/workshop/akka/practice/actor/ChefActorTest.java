package com.autentia.workshop.akka.practice.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import com.autentia.workshop.akka.practice.model.TortillaOrder;
import com.autentia.workshop.akka.practice.model.TortillaType;
import com.autentia.workshop.tortilla.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by blazaro on 1/4/17.
 */
public class ChefActorTest {

    private final ActorSystem actorSystem = ActorSystem.create("testActorSystem");
    private ChefActor chefActor ;
    private TestActorRef<ChefActor> testActorRef;
    private KitchenService kitchenService= mock (KitchenService.class);

    ShopService s;

    @Before
    public void setUp(){
        testActorRef = TestActorRef.create(actorSystem, Props.create(ChefActor.class,kitchenService));
        chefActor = testActorRef.underlyingActor();
    }

    @Test
    public void willBuyIngredientsBeforeCooking() throws Throwable {
        //given
        TortillaOrder tortillaOrder = new TortillaOrder(TortillaType.CON_CEBOLLA, mock(Onions.class) , mock(OliveOil.class) , mock(Potatoes.class) , mock(Eggs.class) , mock(Salt.class) );
        TortillaConCebolla tortilla = mock(TortillaConCebolla.class);

        
        when(kitchenService.cook(any(HotOliveOil.class), any(SlicedPotatoes.class), any(SlicedOnions.class), any(BeatenEggs.class), any(Salt.class))).thenReturn(tortilla);

        //when
        chefActor.onReceive(tortillaOrder);


    }

    @Test
    public void willCookTortillaConCebollaWithKitchenService() throws Throwable {

        //when
        chefActor.onReceive(TortillaType.CON_CEBOLLA);

        //then
        verify(kitchenService.heatOil(any(OliveOil.class)));
        verify(kitchenService.beat(any(Eggs.class)));
        verify(kitchenService.slice(any(Onions.class)));
        verify(kitchenService.slice(any(Potatoes.class)));
        verify(kitchenService.cook(any(HotOliveOil.class), any(SlicedPotatoes.class), any(SlicedOnions.class), any(BeatenEggs.class), any(Salt.class)));


    }


}
