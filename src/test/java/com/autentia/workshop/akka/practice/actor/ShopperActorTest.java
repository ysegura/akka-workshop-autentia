package com.autentia.workshop.akka.practice.actor;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.autentia.workshop.tortilla.*;
import org.junit.Before;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

public class ShopperActorTest {

    private final ActorSystem actorSystem = ActorSystem.create("testActorSystem");

    private ShopperActor shopperActor;

    private ShopService shopService = mock(ShopService.class);
    private final Onions onions = mock(Onions.class);
    private final Potatoes potatoes = mock(Potatoes.class);
    private final Eggs eggs = mock(Eggs.class);
    private final OliveOil oliveOil = mock(OliveOil.class);
    private final Salt salt = mock(Salt.class);

    private final TortillaConCebolla tortillaConCebolla = mock(TortillaConCebolla.class);

    private final TortillaSinCebolla tortillaSinCebolla = mock(TortillaSinCebolla.class);

    @Before
    public void setUp() {
        final TestActorRef<ShopperActor> testActorRef = TestActorRef.create(actorSystem, Props.create(ShopperActor.class, shopService));
        shopperActor = testActorRef.underlyingActor();
    }

    @Test
    public void shouldBuyAllIngredientsWhenOnionsAsked() throws Throwable {
        // When
        shopperActor.onReceive(tortillaConCebolla);
        // Then
        verify(shopService).buyOnions();
        verify(shopService).buyPotatoes();
        verify(shopService).buyEggs();
        verify(shopService).buyOliveOil();
        verify(shopService).buySalt();
    }

    @Test
    public void shouldBuyAllIngredientsButOnionsWhenNoOnionsAsked() throws Throwable {
        // When
        shopperActor.onReceive(tortillaConCebolla);
        // Then
        verify(shopService).buyPotatoes();
        verify(shopService).buyEggs();
        verify(shopService).buyOliveOil();
        verify(shopService).buySalt();
    }

}
