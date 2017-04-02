package com.autentia.workshop.akka.practice.actor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.autentia.workshop.akka.practice.model.TortillaOrder;
import com.autentia.workshop.akka.practice.model.TortillaType;
import com.autentia.workshop.tortilla.*;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

/**
 * Created by blazaro on 1/4/17.
 */
public class CheffActorTest {

    private final ActorSystem actorSystem = ActorSystem.create("testActorSystem");

    private CheffActor cheffActor;

    private TestActorRef<WaiterActor> waiterActor;

    private TestActorRef<CheffActor> testActorRef;

    private WaiterService waiterService = mock(WaiterService.class);

    private KitchenService kitchenService = mock(KitchenService.class);

    private TortillaOrder tortillaConCebollaOrder = new TortillaOrder(TortillaType.CON_CEBOLLA, mock(Onions.class),
            mock(OliveOil.class), mock(Potatoes.class), mock(Eggs.class), mock(Salt.class));

    private TortillaConCebolla tortillaConCebolla = mock(TortillaConCebolla.class);

    private TortillaOrder tortillaSinCebollaOrder = new TortillaOrder(TortillaType.SIN_CEBOLLA, null,
            mock(OliveOil.class), mock(Potatoes.class), mock(Eggs.class), mock(Salt.class));

    private TortillaSinCebolla tortillaSinCebolla = mock(TortillaSinCebolla.class);

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        waiterActor = spy(TestActorRef.create(actorSystem, Props.create(WaiterActor.class, waiterService)));
        testActorRef = TestActorRef.create(actorSystem, Props.create(CheffActor.class, kitchenService, waiterActor));
        cheffActor = testActorRef.underlyingActor();
    }

    @Test
    public void willCookTortillaConCebollaWithKitchenService() throws Throwable {
        TortillaOrder order = trainKitchenServiceMockConCebolla(mock(TortillaOrder.class));
        // when
        cheffActor.onReceive(order);

        // then
        verify(kitchenService).heatOil(any(OliveOil.class));
        verify(kitchenService).beat(any(Eggs.class));
        verify(kitchenService).slice(any(Onions.class));
        verify(kitchenService).slice(any(Potatoes.class));
        verify(kitchenService).cook(any(HotOliveOil.class), any(SlicedPotatoes.class), any(SlicedOnions.class),
                any(BeatenEggs.class), any(Salt.class));
    }

    private TortillaOrder trainKitchenServiceMockConCebolla(TortillaOrder order) {
        when(order.getTortillaType()).thenReturn(TortillaType.CON_CEBOLLA);
        trainKitchenServiceMock();
        when(kitchenService.cook(any(HotOliveOil.class), any(SlicedPotatoes.class), any(SlicedOnions.class),
                any(BeatenEggs.class), any(Salt.class))).thenReturn(mock(TortillaConCebolla.class));
        return order;
    }

    private void trainKitchenServiceMock() {
        when(kitchenService.heatOil(any(OliveOil.class))).thenReturn(mock(HotOliveOil.class));
        when(kitchenService.beat(any(Eggs.class))).thenReturn(mock(BeatenEggs.class));
        when(kitchenService.slice(any(Onions.class))).thenReturn(mock(SlicedOnions.class));
        when(kitchenService.slice(any(Potatoes.class))).thenReturn(mock(SlicedPotatoes.class));
    }

    @Test
    public void willCookTortillaSinCebollaWithKitchenService() throws Throwable {
        // given
        TortillaOrder order = trainKitchenServiceMockSinCebolla(mock(TortillaOrder.class));
        // when
        cheffActor.onReceive(order);

        // then
        verify(kitchenService).heatOil(any(OliveOil.class));
        verify(kitchenService).beat(any(Eggs.class));
        verify(kitchenService).slice(any(Potatoes.class));
        verify(kitchenService, never()).slice(any(Onions.class));
        verify(kitchenService).cook(any(HotOliveOil.class), any(SlicedPotatoes.class), any(BeatenEggs.class),
                any(Salt.class));
    }

    private TortillaOrder trainKitchenServiceMockSinCebolla(TortillaOrder order) {
        when(order.getTortillaType()).thenReturn(TortillaType.SIN_CEBOLLA);
        trainKitchenServiceMock();
        when(kitchenService.cook(any(HotOliveOil.class), any(SlicedPotatoes.class), any(BeatenEggs.class),
                any(Salt.class))).thenReturn(mock(TortillaSinCebolla.class));
        return order;
    }

    @Test
    public void willTellWaiterWhenTortillaConCebollaReady() throws Throwable {
        // given
        TortillaOrder order = mock(TortillaOrder.class);
        when(order.getTortillaType()).thenReturn(TortillaType.CON_CEBOLLA);
        trainKitchenServiceMock();
        when(kitchenService.cook(any(HotOliveOil.class), any(SlicedPotatoes.class), any(SlicedOnions.class),
                any(BeatenEggs.class), any(Salt.class))).thenReturn(mock(TortillaConCebolla.class));
        // when
        cheffActor.onReceive(order);

        // then
        verify(waiterActor).tell(any(TortillaConCebolla.class), isA(ActorRef.class));
    }

    @Test
    public void willTellWaiterWhenTortillaSinCebollaReady() throws Throwable {
        // given
        TortillaOrder order = mock(TortillaOrder.class);
        when(order.getTortillaType()).thenReturn(TortillaType.SIN_CEBOLLA);
        trainKitchenServiceMock();
        when(kitchenService.cook(any(HotOliveOil.class), any(SlicedPotatoes.class), any(BeatenEggs.class),
                any(Salt.class))).thenReturn(mock(TortillaSinCebolla.class));
        // when
        cheffActor.onReceive(order);

        // then
        verify(waiterActor).tell(any(TortillaSinCebolla.class), isA(ActorRef.class));
    }
}
