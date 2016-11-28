package com.cmpe.raft.consensus.jobs;

import com.cmpe.raft.consensus.client.NodeClient;
import com.cmpe.raft.consensus.node.Node;

import java.util.concurrent.*;

/**
 * Created by Sushant on 26-11-2016.
 */
public class HeartBeatJob {

    private String  host;
    private Integer port;

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public HeartBeatJob(String  host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void sendHeartBeat() {
        final Runnable heartBeat = new Runnable() {
            public void run() {
                System.out.println(HeartBeatJob.class.getCanonicalName() +" Hear beat signal to "+host+":"+port);
                NodeClient client = new NodeClient(host, port);
                client.sendHeartBeat();
            }
        };

        final ScheduledFuture<?> heartBeatHandle =
                scheduler.scheduleAtFixedRate(heartBeat, 0, 2, TimeUnit.SECONDS);

        scheduler.schedule(new Runnable() {
            public void run() {
                heartBeatHandle.cancel(true);
            }
        }, 365*10 , TimeUnit.DAYS);  //TODO: This should be configurable, for now running it for 10 years
    }
}
