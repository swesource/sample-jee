package com.swesource.sample.jee.mdb;

/**
 *
 */
public class SyncClass {

    public static synchronized void doStuff(String msg) {
        System.out.println("SyncClass.doStuff: " + msg);
        /*
        try {
            Thread.sleep(3000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
    }
}
