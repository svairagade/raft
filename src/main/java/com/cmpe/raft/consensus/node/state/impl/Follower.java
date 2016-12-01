package com.cmpe.raft.consensus.node.state.impl;


import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.model.AddNode;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.Node;
import com.cmpe.raft.consensus.node.state.NodeState;
import com.cmpe.raft.consensus.util.ServiceUtil;
import com.cmpe.raft.consensus.util.StopWatch;

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

    @Override
    public void performTask() {
        System.out.println(Follower.class.getCanonicalName() + " STATE CHANGED");
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
        if (node.getTerm() < term) {
            return ServiceUtil.constructVote(node, true);     // Yes, I vote for you, as you are better leader than my current leader
        } else {
            return ServiceUtil.constructVote(node, false);    // Sorry, I'm already following someone better
        }
    }

    @Override
    public AddNode addNode(AddNode addNode) {
        Application.addNode(addNode);
        return addNode;
    }

    @Override
    public Object call() throws Exception {

        node.setCurrentState(node.getCandidateState());
        return null;
    }
}
