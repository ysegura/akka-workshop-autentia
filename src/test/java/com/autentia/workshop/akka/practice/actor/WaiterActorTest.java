package com.autentia.workshop.akka.practice.actor;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import com.autentia.workshop.tortilla.Tortilla;
import com.autentia.workshop.tortilla.WaiterService;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

/**
 * Created by blazaro on 1/4/17.
 */
public class WaiterActorTest {

    private final ActorSystem actorSystem = ActorSystem.create("testActorSystem");
    private WaiterActor waiterActor ;
    private WaiterService waiterService= mock (WaiterService.class);

    @Before
    public void setUp(){
        final TestActorRef<WaiterActor> testActorRef= TestActorRef.create(actorSystem, Props.create(WaiterActor.class,waiterService));
        waiterActor = testActorRef.underlyingActor();
    }

    @Test
    public void willUseWaiterServiceForServeTorillas() throws Throwable {

        //given
        Tortilla tortilla = mock(Tortilla.class);

        //when
        waiterActor.onReceive(tortilla);

        //then
        verify(waiterService).serveTortilla(tortilla);

    }




}
