/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swesource.sample.jee.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

/**
 *
 * @author arnie
 */
public class SampleProducer {
    
    //@Resource(mappedName="java:/InVMConnectionFactory")
    //private static ConnectionFactory connectionFactory;
    
    //@Resource(mappedName="/queue/SampleQueue")
    //private static Queue queue;

    public static void main(String[] argv) throws Exception {
        System.out.println("SampleProducer about to send messages.");
        Properties p = new Properties( );
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        p.put(Context.URL_PKG_PREFIXES," org.jboss.naming:org.jnp.interfaces");
        p.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        InitialContext jndiContext = new InitialContext(p);
        ConnectionFactory connectionFactory = (ConnectionFactory)jndiContext.lookup("/ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue)jndiContext.lookup("/queue/SampleQueue");
        MessageProducer producer = session.createProducer(queue);
        TextMessage message = session.createTextMessage();
        for (int i=0 ; i<10 ; i++) {
            message.setText("Message" + i);
            System.out.println("SampleProducer sends " + message.getText());
            producer.send(message);
        }
        System.out.println("SampleProducer has send all messages.");
    }
}
