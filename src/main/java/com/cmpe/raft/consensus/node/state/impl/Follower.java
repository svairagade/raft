package com.cmpe.raft.consensus.node.state.impl;


import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.client.NodeClient;
import com.cmpe.raft.consensus.dao.Dao;
import com.cmpe.raft.consensus.model.AddNode;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Person;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.Node;
import com.cmpe.raft.consensus.node.state.NodeState;
import com.cmpe.raft.consensus.util.ServiceUtil;
import com.cmpe.raft.consensus.util.StopWatch;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Sushant on 25-11-2016.
 */
public class Follower implements NodeState, Callable {

    private StopWatch stopWatch;
    private Node node;
    private NodeClient leaderNodeClient;

    public Follower(Node node) {
        super();
        this.node = node;
        stopWatch = new StopWatch(10000, this);
        leaderNodeClient = new NodeClient(node.getLeaderHost(), node.getLeaderPort());
    }

    @Override
    public void performTask() {
        System.out.println(Follower.class.getCanonicalName() + " STATE CHANGED");
        stopWatch.start();
        if(!(leaderNodeClient.getHost().equals(node.getLeaderHost()) && leaderNodeClient.getPort().equals(node.getLeaderPort()))) {
            leaderNodeClient.updateHost(node.getLeaderHost(), node.getLeaderPort());
            List<Person> persons = leaderNodeClient.sendDoGetAllRequest();
            Dao.updateDao(persons);
        }
    }

    @Override
    public HeartBeat onHeartBeat(long term, String host, int port) {
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

    @Override
    public String getName() {
        return "FOLLOWER";
    }

    @Override
    public void stopJobs() {
        // No jobs for this state
    }
}
