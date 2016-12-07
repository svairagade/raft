package com.cmpe.raft.consensus.jobs;

/**
 * Created by Sushant on 06-12-2016.
 */
public class WorkJob {
    private static final String QUEUE_HOST = "localhost";
    private static final String REQUEST_QUEUE = "request_queue";
    private static final String RESPONSE_QUEUE = "response_queue";

    public void listen() {
        //send relevant requests to all other nodes in the cluster
    }

    public void stopListeing() {
        //Stop listening
    }

}
