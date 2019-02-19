package com.veryqy.artemis.pool;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;


public class JmsConnectionPoolFactory extends BasePooledObjectFactory<Connection> {

    private static final Logger log = LoggerFactory.getLogger(JmsConnectionPoolFactory.class);

    private ConnectionOptions options;

    public JmsConnectionPoolFactory(ConnectionOptions options){
        this.options=options;
    }

    public Connection create() throws Exception {
        log.debug("JmsConnectionPoolFactory.create............start");
        ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://artemis:8040");//61616
        Connection connection = cf.createConnection();
        log.debug("JmsConnectionPoolFactory.create............end");
        connection.start();
        return connection;
    }

    public PooledObject<Connection> wrap(Connection obj) {
        return new DefaultPooledObject<Connection>(obj);
    }
}
