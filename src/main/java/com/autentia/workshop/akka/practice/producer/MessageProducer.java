package com.autentia.workshop.akka.practice.producer;

        import com.autentia.workshop.akka.practice.model.Event;
        import com.autentia.workshop.akka.practice.model.Request;
        import com.autentia.workshop.akka.practice.model.TortillaType;
        import com.rabbitmq.client.AMQP;
        import com.rabbitmq.client.Channel;
        import com.rabbitmq.client.Connection;
        import com.rabbitmq.client.ConnectionFactory;
        import com.rabbitmq.client.AMQP.BasicProperties;
        import java.io.IOException;
        import java.net.URISyntaxException;
        import java.security.KeyManagementException;
        import java.security.NoSuchAlgorithmException;
        import java.util.Random;
        import java.util.UUID;
        import java.util.concurrent.TimeoutException;
        import java.util.concurrent.atomic.AtomicInteger;
        import org.apache.commons.lang3.SerializationUtils;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

public class MessageProducer {
    private static final String HOST_NAME = "5.56.60.112";
    private static final int PORT_NUMBER = 5672;
    private static final String EXCHANGE_NAME = "nig_team_in_exchange";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public MessageProducer() {
    }

    public static void main(String[] args) throws InterruptedException {
        MessageProducer producer = new MessageProducer();
        producer.execute();
    }

    public void execute() throws InterruptedException {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri("amqp://guest:guest@5.56.60.112:5672/default");
            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();
            Random rand = new Random();
            AtomicInteger requestId = new AtomicInteger(100);
            AtomicInteger customerId = new AtomicInteger(1);

            while(true) {
                int type = rand.nextInt(2);
                TortillaType tortillaType;
                if(type == 0) {
                    tortillaType = TortillaType.SIN_CEBOLLA;
                } else {
                    tortillaType = TortillaType.CON_CEBOLLA;
                }

                Request request = new Request(String.valueOf(customerId.getAndIncrement()), String.valueOf(requestId.getAndIncrement()), tortillaType);
                Event event = new Event(UUID.randomUUID().toString(), request);
                channel.basicPublish(EXCHANGE_NAME, "", (AMQP.BasicProperties)null, SerializationUtils.serialize(event));
                this.log.info("------------ Publish " + tortillaType);
                //Thread.sleep(500L);
            }
        } catch (TimeoutException | KeyManagementException | NoSuchAlgorithmException | URISyntaxException | IOException var11) {
            throw new RuntimeException(var11);
        }
    }
}
