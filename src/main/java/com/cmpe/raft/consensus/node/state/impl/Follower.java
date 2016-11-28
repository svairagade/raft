package com.cmpe.raft.consensus.node.state.impl;


import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.Node;
import com.cmpe.raft.consensus.node.state.NodeState;
import com.cmpe.raft.consensus.util.ServiceUtil;
import com.cmpe.raft.consensus.util.StopWatch;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Created by Sushant on 25-11-2016.
 */
public class Follower implements NodeState, Callable {

    private StopWatch stopWatch;
    private Node node;

    public Follower(Node node) {
        super();
        this.node = node;
        stopWatch = new StopWatch(10000, this);
    }

    public void performTask() {
        System.out.println(Follower.class.getCanonicalName() + " perform task.");
        stopWatch.start();
    }

    @Override
    public HeartBeat onHeartBeat(long term) {
        System.out.println(Follower.class.getCanonicalName() + " received Heart beat.");
        stopWatch.reset();
        if (node.getTerm() < term) {
            node.setTerm(term);
        }
        return ServiceUtil.constructHeartBeat(node);
    }

    @Override
    public Vote onCandidacyRequest(long term) {
        Vote vote = new Vote();
        vote.setPort(Application.getPort());
        vote.setIp(Application.getIp());
        vote.setDate(new Date());
        if(node.getTerm() < term) {
            vote.setVote(true);     // Yes, I vote for you, as you are better leader than my current leader
        } else {
            vote.setVote(false);    // Sorry, I'm already following someone better
        }
        return vote;
    }

    @Override
    public Object call() throws Exception {
        node.setCurrentState(node.getCandidateState());
        return null;
    }
}
