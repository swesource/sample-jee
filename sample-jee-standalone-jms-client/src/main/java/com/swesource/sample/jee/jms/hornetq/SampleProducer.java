/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swesource.sample.jee.jms.hornetq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
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
        //p.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        //p.put(Context.URL_PKG_PREFIXES," org.jboss.naming:org.jnp.interfaces");
        //p.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        p.put(Context.PROVIDER_URL, "remote://127.0.0.1:4447");
        // Credentials should not(!) be added through properties as below.
        //p.put(Context.SECURITY_PRINCIPAL, "admin");
        //p.put(Context.SECURITY_CREDENTIALS, "admin2");
        Context jndiContext = new InitialContext(p);
        ConnectionFactory connectionFactory = (ConnectionFactory)jndiContext.lookup("jms/RemoteConnectionFactory");
        Connection connection = connectionFactory.createConnection("admin", "admin2");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = (Destination)jndiContext.lookup("jms/queue/SampleQueue");
        //Queue queue = (Queue)jndiContext.lookup("jms/queue/SampleQueue");
        //MessageProducer producer = session.createProducer(queue);
        MessageProducer producer = session.createProducer(destination);
        TextMessage message = session.createTextMessage();
        for (int i=0 ; i<10 ; i++) {
            //message.setText("Message" + i);
            message.setText("" + i);
            System.out.println("SampleProducer sends " + message.getText());
            producer.send(message);
        }
        System.out.println("SampleProducer has send all messages.");
    }
}
