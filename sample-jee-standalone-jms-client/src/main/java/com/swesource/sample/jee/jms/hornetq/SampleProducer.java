package com.swesource.sample.jee.jms.hornetq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public final class SampleProducer {

    private static final int MESSAGE_COUNT = 10;
    private static final int ERROR_EXIT_CODE = -1;
    private static final Logger LOGGER = Logger.getLogger(SampleProducer.class.getName());

    /*
    @Resource(mappedName="java:/InVMConnectionFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(mappedName="/queue/SampleQueue")
    private static Queue queue;
    */

    private SampleProducer() {
    }

    public static void main(String[] argv) {
        LOGGER.info("SampleProducer about to send messages.");
        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        p.put(Context.PROVIDER_URL, "remote://127.0.0.1:4447");
        // Credentials should not(!) be added through properties as below.

        Context jndiContext = null;
        ConnectionFactory connectionFactory = null;
        // Use Destination OR Queue
        Destination destination = null;
        Queue queue = null;
        try {
            jndiContext = new InitialContext(p);
            connectionFactory = (ConnectionFactory)jndiContext.lookup("jms/RemoteConnectionFactory");
            destination = (Destination)jndiContext.lookup("jms/queue/SampleQueue");
            queue = (Queue)jndiContext.lookup("jms/queue/SampleQueue");
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(ERROR_EXIT_CODE);
        }

        Connection connection = null;
        // A Producer can be created from a Session with a Destination OR a Queue
        MessageProducer producer1 = null;
        MessageProducer producer2 = null;
        TextMessage message1 = null;
        TextMessage message2 = null;
        try {
            connection = connectionFactory.createConnection("admin", "admin2");
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer1 = session.createProducer(destination);
            producer2 = session.createProducer(queue);
            message1 = session.createTextMessage();
            message2 = session.createTextMessage();
        }
        catch (JMSException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            closeConnection(connection);
            System.exit(ERROR_EXIT_CODE);
        }

        for (int i=0 ; i<MESSAGE_COUNT ; i++) {
            try {
                message1.setText("SentByDestination" + i);
                message2.setText("SentByQueue" + i);
                LOGGER.info("SampleProducer sends " + message1.getText());
                LOGGER.info("SampleProducer sends " + message2.getText());
                producer1.send(message1);
                producer2.send(message2);
            } catch (JMSException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                closeConnection(connection);
                System.exit(ERROR_EXIT_CODE);
            }
        }
        LOGGER.info("SampleProducer has send all messages.");
        closeConnection(connection);
    }

    private static void closeConnection(Connection connection) {
        if(connection != null) {
            try {
                connection.close();
            }
            catch (JMSException e) {
                LOGGER.log(Level.SEVERE, "Couldn't close connection.", e);
                System.exit(ERROR_EXIT_CODE);
            }
        }
    }
}
