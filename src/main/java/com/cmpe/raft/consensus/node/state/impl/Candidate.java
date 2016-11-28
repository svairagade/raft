package com.cmpe.raft.consensus.node.state.impl;


import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.Node;
import com.cmpe.raft.consensus.node.state.NodeState;
import com.cmpe.raft.consensus.util.ServiceUtil;

import java.util.Date;

/**
 * Created by Sushant on 25-11-2016.
 */
public class Candidate implements NodeState {

    private Node node;

    public Candidate(Node node) {
        super();
        this.node = node;
    }

    public void performTask() {
        //TODO: send candidacy request to all the nodes in the cluster and wait till it gets maximum votes or finds better candidate
    }

    @Override
    public HeartBeat onHeartBeat(long term) {
        System.out.println(Candidate.class.getCanonicalName() + " received Heart beat.");
        if (node.getTerm() < term) {
            node.setTerm(term);
            node.setCurrentState(node.getFollowerState());
        }
        return ServiceUtil.constructHeartBeat(node);
    }

    @Override
    public Vote onCandidacyRequest(long term) {
        Vote vote = new Vote();
        vote.setIp(Application.getIp());
        vote.setPort(Application.getPort());
        vote.setDate(new Date());
        vote.setVote(false);  //I'm a candidate too, so sorry
        return vote;
    }
}
