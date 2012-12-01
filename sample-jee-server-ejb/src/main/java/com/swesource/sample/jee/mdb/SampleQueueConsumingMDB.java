package com.swesource.sample.jee.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MDB accepting text messages and calling a synchronized resource - the class
 * @see com.swesource.sample.jee.mdb.SyncClass .
 * @author Arnold Johansson
 */

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jboss/exported/jms/queue/SampleQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class SampleQueueConsumingMDB implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(SampleQueueConsumingMDB.class.getName());
    private static final long SLEEP_TIME_MS = 5000L;

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
                    LOGGER.log(Level.SEVERE, "SampleQueueConsumingMDB Couldn't get text in message.", e);
                    return;
                }
                LOGGER.info("SampleQueueConsumingMDB (" + Thread.currentThread().getId() + ") : Received message: " + msg);
                // Very non-JEE...
                if (msg.equals("5")) {
                    try {
                        Thread.sleep(SLEEP_TIME_MS);
                    } catch (InterruptedException e) {
                        LOGGER.log(Level.SEVERE, "SampleQueueConsumingMDB (" + Thread.currentThread().getId() + ") " + e);
                    }
                    throw new RuntimeException("Simulated exception from SampleQueueConsumingMDB.");
                }
                SyncClass.doStuff(msg);
                LOGGER.info("SampleQueueConsumingMDB: Exiting for message:" + msg);
            } else {
                LOGGER.info("SampleQueueConsumingMDB: Not a TextMessage.");
            }
        }
    }
}
