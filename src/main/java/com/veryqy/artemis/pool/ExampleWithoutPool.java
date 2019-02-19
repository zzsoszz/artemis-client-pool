package com.veryqy.artemis.pool;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

public class ExampleWithoutPool {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://192.168.1.101:8040");
        Connection connection = cf.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("bus.test.exampleQueue");

        MessageProducer producer = session.createProducer(queue);

        TextMessage message = session.createTextMessage("This is a text message");

        System.out.println("Sending message [" + message.getText() +  "]" );

        producer.send(message);

        MessageConsumer messageConsumer = session.createConsumer(queue);

        connection.start();

        TextMessage messageReceived = (TextMessage) messageConsumer.receive(5000);
        System.out.println(messageReceived.getText());
    }
}
