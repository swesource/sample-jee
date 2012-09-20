/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swesource.sample.jee.jms.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.util.IndentPrinter;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author arnie
 */
public class SampleProducer extends Thread {

    private static int parallelThreads = 1;
    private String username = ActiveMQConnection.DEFAULT_USER;
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;    // failover://tcp://localhost:61616
    private boolean transacted = true;
    private boolean persistent = true;
    private boolean topic = false; // true=>topic ; false=>queue
    private Destination destination;
    private String subject = "SAMPLE.DEFAULT";
    private long timeToLive;
    private int messageSize = 255;
    private boolean verbose = true;
        private static Object lockResults = new Object();
    private int messageCount = 10;
    private long sleepTime;

    public static void main(String[] args) {
        System.out.println("SampleProducer about to send messages.");

        ArrayList<SampleProducer> threads = new ArrayList();
        SampleProducer producer = new SampleProducer();
        /*
        String[] unknown = CommandLineSupport.setOptions(producer, args);
        if (unknown.length > 0) {
            System.out.println("Unknown options: " + Arrays.toString(unknown));
            System.exit(-1);
        }
        */
        //producer.showParameters();
        for (int threadCount = 1; threadCount <= parallelThreads; threadCount++) {
            producer = new SampleProducer();
            //CommandLineSupport.setOptions(producer, args);
            producer.start();
            threads.add(producer);
        }

        while (true) {
            Iterator<SampleProducer> itr = threads.iterator();
            int running = 0;
            while (itr.hasNext()) {
                SampleProducer thread = itr.next();
                if (thread.isAlive()) {
                    running++;
                }
            }
            if (running <= 0) {
                System.out.println("All threads completed their work");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }

    public void showParameters() {
        System.out.println("Connecting to URL: " + url + " (" + username + ":" + password + ")");
        System.out.println("Publishing a Message with size " + messageSize + " to " + (topic ? "topic" : "queue") + ": " + subject);
        System.out.println("Using " + (persistent ? "persistent" : "non-persistent") + " messages");
        System.out.println("Sleeping between publish " + sleepTime + " ms");
        System.out.println("Running " + parallelThreads + " parallel threads");

        if (timeToLive != 0) {
            System.out.println("Messages time to live " + timeToLive + " ms");
        }
    }

    public void run() {
        Connection connection = null;
        try {
            // Create the connection.
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(username, password, url);
            connection = connectionFactory.createConnection();
            connection.start();

            // Create the session
            Session session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
            if (topic) {
                destination = session.createTopic(subject);
            } else {
                destination = session.createQueue(subject);
            }

            // Create the producer.
            MessageProducer producer = session.createProducer(destination);
            if (persistent) {
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            } else {
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            }
            if (timeToLive != 0) {
                producer.setTimeToLive(timeToLive);
            }

            // Start sending messages
            sendLoop(session, producer);

            System.out.println("[" + this.getName() + "] Done.");

            synchronized (lockResults) {
                ActiveMQConnection c = (ActiveMQConnection) connection;
                System.out.println("[" + this.getName() + "] Results:\n");
                c.getConnectionStats().dump(new IndentPrinter());
            }

        } catch (Exception e) {
            System.out.println("[" + this.getName() + "] Caught: " + e);
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Throwable ignore) {
            }
        }
    }

    protected void sendLoop(Session session, MessageProducer producer) throws Exception {

        for (int i = 0; i < messageCount || messageCount == 0; i++) {

            TextMessage message = session.createTextMessage(createMessageText(i));

            if (verbose) {
                String msg = message.getText();
                if (msg.length() > 50) {
                    msg = msg.substring(0, 50) + "...";
                }
                System.out.println("[" + this.getName() + "] Sending message: '" + msg + "'");
            }

            producer.send(message);

            if (transacted) {
                System.out.println("[" + this.getName() + "] Committing " + messageCount + " messages");
                session.commit();
            }
            Thread.sleep(sleepTime);
        }
    }

    private String createMessageText(int index) {
        StringBuffer buffer = new StringBuffer(messageSize);
        buffer.append("Message: " + index + " sent at: " + new Date());
        if (buffer.length() > messageSize) {
            return buffer.substring(0, messageSize);
        }
        for (int i = buffer.length(); i < messageSize; i++) {
            buffer.append(' ');
        }
        return buffer.toString();
    }

    public void setPersistent(boolean durable) {
        this.persistent = durable;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public void setParallelThreads(int parallelThreads) {
        if (parallelThreads < 1) {
            parallelThreads = 1;
        }
        this.parallelThreads = parallelThreads;
    }

    public void setTopic(boolean topic) {
        this.topic = topic;
    }

    public void setQueue(boolean queue) {
        this.topic = !queue;
    }

    public void setTransacted(boolean transacted) {
        this.transacted = transacted;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
