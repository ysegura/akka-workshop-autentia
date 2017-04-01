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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by blazaro on 1/4/17.
 */
public class CheffActorTest {

    private final ActorSystem actorSystem = ActorSystem.create("testActorSystem");
    private CheffActor cheffActor;
    private KitchenService kitchenService= mock (KitchenService.class);
    private TestActorRef waiterActor;
    private WaiterService waiterService= mock (WaiterService.class);


    @Before
    public void setUp(){
        final TestActorRef<WaiterActor> waiterActor= TestActorRef.create(actorSystem, Props.create(WaiterActor.class,waiterService));
        final TestActorRef<CheffActor> testActorRef= TestActorRef.create(actorSystem, Props.create(CheffActor.class,kitchenService,waiterActor));
        cheffActor = testActorRef.underlyingActor();
    }



    @Test
    public void willCookTortillaConCebollaWithKitchenService() throws Throwable {

        //given
        final TortillaOrder tortillaOrder=mock(TortillaOrder.class,RETURNS_DEEP_STUBS);
        when(tortillaOrder.getTortillaType()).thenReturn(TortillaType.CON_CEBOLLA);
        final TortillaConCebolla tortilla=mock(TortillaConCebolla.class);
        when(kitchenService.cook(any(HotOliveOil.class), any(SlicedPotatoes.class), any(SlicedOnions.class), any(BeatenEggs.class), any(Salt.class))).thenReturn(tortilla);

        //when
        cheffActor.onReceive(tortillaOrder);

        //then
        verifyCommonCookingActions();
        verify(kitchenService).slice(any(Onions.class));
        verify(kitchenService).cook(any(HotOliveOil.class), any(SlicedPotatoes.class), any(SlicedOnions.class), any(BeatenEggs.class), any(Salt.class));


    }

    private void verifyCommonCookingActions() {
        verify(kitchenService).heatOil(any(OliveOil.class));
        verify(kitchenService).beat(any(Eggs.class));
        verify(kitchenService).slice(any(Potatoes.class));
    }

    @Test
    public void willCookTortillaSinCebollaWithKitchenService() throws Throwable {

        //given
        final TortillaOrder tortillaOrder=mock(TortillaOrder.class,RETURNS_DEEP_STUBS);
        when(tortillaOrder.getTortillaType()).thenReturn(TortillaType.SIN_CEBOLLA);
        final TortillaSinCebolla tortilla=mock(TortillaSinCebolla.class);
        when(kitchenService.cook(any(HotOliveOil.class), any(SlicedPotatoes.class), any(BeatenEggs.class), any(Salt.class))).thenReturn(tortilla);

        //when
        cheffActor.onReceive(tortillaOrder);

        //then
        verifyCommonCookingActions();
        verify(kitchenService).cook(any(HotOliveOil.class), any(SlicedPotatoes.class), any(BeatenEggs.class), any(Salt.class));


    }


}
