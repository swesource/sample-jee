package com.swesource.sample.jee.jms.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.protobuf.compiler.CommandLineSupport;
import org.apache.activemq.util.IndentPrinter;

import javax.jms.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 *
 */
public class SampleProducer extends Thread {

    private static final Logger LOGGER = Logger.getLogger(SampleProducer.class.getName());
    private static final int ERROR = -1;
    private static final int MESSAGE_COUNT = 10;
    private static final int MESSAGE_SIZE = 255;
    private static final int PARALLELL_THREADS = 1;
    private static final int THREAD_SLEEP_MS = 1000;
    private static final int MAX_MSG_LENGTH = 50;

    private static int parallelThreads = PARALLELL_THREADS;
    private String username = ActiveMQConnection.DEFAULT_USER;
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;
    /* failover://tcp://localhost:61616 */
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private boolean transacted = true;
    private boolean persistent = true;
    /* true=>topic ; false=>queue */
    private boolean topic = false;
    private Destination destination;
    private String subject = "SAMPLE.DEFAULT";
    private long timeToLive;
    private int messageSize = MESSAGE_SIZE;
    private boolean verbose = true;
        private static Object lockResults = new Object();
    private int messageCount = MESSAGE_COUNT;
    private long sleepTime;

    public static void main(String[] args) {
        LOGGER.info("SampleProducer about to send messages.");

        ArrayList<SampleProducer> threads = new ArrayList();
        SampleProducer producer = new SampleProducer();

        String[] unknown = CommandLineSupport.setOptions(producer, args);
        if (unknown.length > 0) {
            LOGGER.info("Unknown options: " + Arrays.toString(unknown));
            System.exit(ERROR);
        }

        producer.showParameters();
        for (int threadCount = 1; threadCount <= parallelThreads; threadCount++) {
            producer = new SampleProducer();
            CommandLineSupport.setOptions(producer, args);
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
                LOGGER.info("All threads completed their work");
                break;
            }
            try {
                Thread.sleep(THREAD_SLEEP_MS);
            } catch (Exception e) {
            }
        }
    }

    public void showParameters() {
        LOGGER.info("Connecting to URL: " + url + " (" + username + ":" + password + ")");
        LOGGER.info("Publishing a Message with size " + messageSize + " to " + (topic ? "topic" : "queue") + ": " + subject);
        LOGGER.info("Using " + (persistent ? "persistent" : "non-persistent") + " messages");
        LOGGER.info("Sleeping between publish " + sleepTime + " ms");
        LOGGER.info("Running " + parallelThreads + " parallel threads");

        if (timeToLive != 0) {
            LOGGER.info("Messages time to live " + timeToLive + " ms");
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

            LOGGER.info("[" + this.getName() + "] Done.");

            synchronized (lockResults) {
                ActiveMQConnection c = (ActiveMQConnection) connection;
                LOGGER.info("[" + this.getName() + "] Results:\n");
                c.getConnectionStats().dump(new IndentPrinter());
            }

        } catch (Exception e) {
            LOGGER.info("[" + this.getName() + "] Caught: " + e);
        } finally {
            try {
                connection.close();
            } catch (JMSException ignore) {
            }
        }
    }

    protected void sendLoop(Session session, MessageProducer producer) throws JMSException, InterruptedException {

        for (int i = 0; i < messageCount || messageCount == 0; i++) {

            TextMessage message = session.createTextMessage(createMessageText(i));

            if (verbose) {
                String msg = message.getText();
                if (msg.length() > MAX_MSG_LENGTH) {
                    msg = msg.substring(0, MAX_MSG_LENGTH) + "...";
                }
                LOGGER.info("[" + this.getName() + "] Sending message: '" + msg + "'");
            }

            producer.send(message);

            if (transacted) {
                LOGGER.info("[" + this.getName() + "] Committing " + messageCount + " messages");
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
            this.parallelThreads = 1;
            return;
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
