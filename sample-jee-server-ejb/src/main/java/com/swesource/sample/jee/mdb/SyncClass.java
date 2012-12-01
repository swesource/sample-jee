package com.swesource.sample.jee.mdb;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A synchronized class performing some work.
 * @author Arnold Joahsson
 */
public final class SyncClass {

    private static final Logger LOGGER = Logger.getLogger(SyncClass.class.getName());
    private static final long THREAD_SLEEP_TIME = 3000L;

    private SyncClass() {}

    public static synchronized void doStuff(String msg) {
        LOGGER.info("SyncClass.doStuff: " + msg);
        try {
            Thread.sleep(THREAD_SLEEP_TIME);
        }
        catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Got interupted!", e);
        }
    }
}
