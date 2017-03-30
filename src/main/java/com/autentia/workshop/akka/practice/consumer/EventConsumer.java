package com.autentia.workshop.akka.practice.consumer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autentia.workshop.akka.practice.executor.MessageExecutor;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class EventConsumer {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final Connection conn;
	private final Channel channel;
	

	public EventConsumer(final String hostName, final int portNumber)   {
		try {
			final ConnectionFactory factory = new ConnectionFactory();
			factory.setUri("amqp://guest:guest@"+hostName+":"+portNumber+"/default");
			
			this.conn = factory.newConnection();
			this.channel = conn.createChannel();
		}catch(IOException | TimeoutException | KeyManagementException | NoSuchAlgorithmException | URISyntaxException e){
			throw new RuntimeException(e);
		}
	}
	
	public void startConsuming(final String queueName, final MessageExecutor messageExecutor) {
		try {
			log.info("-- Start consuming...");
			
			channel.basicConsume(queueName, true, new DefaultConsumer(channel) {

				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {					
					messageExecutor.execute(SerializationUtils.deserialize(body));
				}
				
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public void stop() {
		try {
			channel.close();
			conn.close();
		}catch(IOException | TimeoutException e){
			throw new RuntimeException();
		}
		
	}

}
