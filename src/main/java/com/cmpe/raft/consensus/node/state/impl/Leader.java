package com.cmpe.raft.consensus.node.state.impl;

import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.Node;
import com.cmpe.raft.consensus.node.state.NodeState;
import com.cmpe.raft.consensus.util.ServiceUtil;
import com.sun.deploy.services.Service;
import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider;

import java.util.Date;

/**
 * Created by Sushant on 25-11-2016.
 */
public class Leader implements NodeState {
    private Node node;

    public Leader(Node node) {
        super();
        this.node = node;
    }

    public void performTask() {
        //TODO: yay, I've to send hear beast to all
    }

    @Override
    public HeartBeat onHeartBeat(long term) {
        if (node.getTerm() < term) {
            //I resign from my post as a Leader as you are better than me and I will follow your footsteps
            node.setTerm(term);
            node.setCurrentState(node.getFollowerState());
        }
        return ServiceUtil.constructHeartBeat(node);
    }

    @Override
    public Vote onCandidacyRequest(long term) {
        Vote vote = new Vote();
        vote.setPort(Application.getPort());
        vote.setIp(Application.getIp());
        vote.setDate(new Date());
        vote.setVote(false);    // You wish ! I'm not dead yet
        return vote;
    }
}
