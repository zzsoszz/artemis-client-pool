package com.veryqy.artemis.pool;

import org.apache.activemq.artemis.api.core.ActiveMQNotConnectedException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.*;


public class MessageTemplate {

    private static final Logger log = LoggerFactory.getLogger(MessageTemplate.class);

    private GenericObjectPool<Connection> pool;

    public MessageTemplate(ConnectionOptions options){
        GenericObjectPoolConfig genericObjectPoolConfig=new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(10);
        this.pool=new GenericObjectPool<Connection>(new JmsConnectionPoolFactory(options), genericObjectPoolConfig);
    }

    public void send(String queueName,String content) throws Exception {
        Connection connection=null;
        try{
            log.debug("created:"+pool.getCreatedCount()+" destroyed:"+pool.getDestroyedCount()+" brorrowed:"+pool.getBorrowedCount()+" returned:"+pool.getReturnedCount()+" waiters:"+pool.getNumWaiters()+" actives:"+pool.getNumActive()+" idles:"+pool.getNumIdle());
            connection= pool.borrowObject();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage(content);
            log.debug("Sending message [" + message.getText() + "]");
            producer.send(message);
            session.close();
            pool.returnObject(connection);
        }catch (javax.jms.JMSException ex){
            ex.printStackTrace();
            if(ex.getCause() instanceof  ActiveMQNotConnectedException && connection!=null)
            {
               pool.invalidateObject(connection);
            }
            throw ex;
        }
    }


    public String receive(String queueName) throws Exception {
        Connection connection=null;
        try{
            log.debug("created:"+pool.getCreatedCount()+" destroyed:"+pool.getDestroyedCount()+" brorrowed:"+pool.getBorrowedCount()+" returned:"+pool.getReturnedCount()+" waiters:"+pool.getNumWaiters()+" actives:"+pool.getNumActive()+" idles:"+pool.getNumIdle());
            connection= pool.borrowObject();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);
            MessageConsumer messageConsumer = session.createConsumer(queue);
            TextMessage msg=(TextMessage)messageConsumer.receive();
            log.debug("recived message [" + msg.getText() + "]");
            String content=msg.getText();
            session.close();
            pool.returnObject(connection);
            return content;
        }catch (javax.jms.JMSException ex){
            ex.printStackTrace();
            if(ex.getCause() instanceof  ActiveMQNotConnectedException && connection!=null)
            {
                pool.invalidateObject(connection);
            }
            throw ex;
        }
    }

}
