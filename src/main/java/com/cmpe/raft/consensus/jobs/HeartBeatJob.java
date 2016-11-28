package com.cmpe.raft.consensus.jobs;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sushant on 26-11-2016.
 */
public class HeartBeatJob {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void sendHeartBeat() {
        final Runnable heartBeat = new Runnable() {
            public void run() {
                System.out.println("Hearbeat signals...");
            }
        };

        final ScheduledFuture<?> heartBeatHandle =
                scheduler.scheduleAtFixedRate(heartBeat, 2, 2, TimeUnit.SECONDS);

        /*scheduler.schedule(new Runnable() {
            public void run() {
                heartBeatHandle.cancel(true);
            }
        }, 60 * 60, TimeUnit.SECONDS);*/
    }
}
