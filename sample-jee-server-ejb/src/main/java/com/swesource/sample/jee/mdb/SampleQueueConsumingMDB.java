package com.swesource.sample.jee.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jboss/exported/jms/queue/SampleQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
//        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1")
        //@ActivationConfigProperty(propertyName = "user", propertyValue = "admin")
        //@ActivationConfigProperty(propertyName = "password", propertyValue = "admin2")
})
public class SampleQueueConsumingMDB implements MessageListener {

    public SampleQueueConsumingMDB() {
    }

    @Override
    public void onMessage(Message message) {

        synchronized (this) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;

            String msg = null;
            try {
                msg = textMessage.getText();
            } catch (JMSException e) {
                e.printStackTrace();
            }
            System.out.println("SampleQueueConsumingMDB (" + Thread.currentThread().getId() + ") : Received message: " + msg);
            // Very non-JEE...
            /*
            if (msg.equals("5")) {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                throw new RuntimeException("Simulated exception from SampleQueueConsumingMDB.");

            }
            */
            SyncClass.doStuff(msg);
            System.out.println("SampleQueueConsumingMDB: Exiting for message:" + msg);
        }
        else {
            System.out.println("SampleQueueConsumingMDB: Not a TextMessage.");
        }
        }
    }
}
