package com.autentia.workshop.akka.practice.producer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autentia.workshop.messagebroker.Event;
import com.autentia.workshop.messagebroker.Payload;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EventProducer {

	private static final String HOST_NAME = "5.56.60.112";
	private static final int PORT_NUMBER = 5672;
	private static final String EXCHANGE_NAME = "workshop_exchange";
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) throws InterruptedException {
		final EventProducer producer = new EventProducer();
		producer.execute();
	}

	public void execute() throws InterruptedException {
		try {
			final ConnectionFactory factory = new ConnectionFactory();
			factory.setUri("amqp://guest:guest@" + HOST_NAME + ":" + PORT_NUMBER + "/default");

			final Connection conn = factory.newConnection();
			final Channel channel = conn.createChannel();
			
			for(int i=0; i<10; i++) {
				final Event event = new Event(String.valueOf(i), new Payload());
				channel.basicPublish(EXCHANGE_NAME, "", null, SerializationUtils.serialize(event));
				log.info("Send event:"+event.getId()+" to "+EXCHANGE_NAME);
				Thread.sleep(100);
			}
			
			
			conn.close();
			
		} catch (IOException | TimeoutException | KeyManagementException | NoSuchAlgorithmException	| URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}
