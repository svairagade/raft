package com.cmpe.raft.consensus.node;

import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.state.NodeState;
import com.cmpe.raft.consensus.node.state.impl.Candidate;
import com.cmpe.raft.consensus.node.state.impl.Follower;
import com.cmpe.raft.consensus.node.state.impl.Leader;

/**
 * Created by Sushant on 26-11-2016.
 */
public class Node {

    private static Node nodeInstance;
    private NodeState currentState;
    private NodeState candidateState;
    private NodeState followerState;
    private NodeState leaderState;
    private Long term = new Long(0);

    private Node() {
        candidateState = new Candidate(this);
        followerState = new Follower(this);
        leaderState = new Leader(this);
        this.setCurrentState(followerState); //default state
    }

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public static Node getInstance() {
        if(nodeInstance == null) {
            nodeInstance = new Node();
        }
        return nodeInstance;
    }

    public NodeState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(NodeState currentState) {
        this.currentState = currentState;
        this.currentState.performTask();
    }

    public NodeState getCandidateState() {
        return candidateState;
    }

    public NodeState getFollowerState() {
        return followerState;
    }

    public NodeState getLeaderState() {
        return leaderState;
    }

    public HeartBeat reactToHeartBeat(long term) {
        return currentState.onHeartBeat(term);
    }

    public Vote reactToLeaderRequest(long term) {
        return currentState.onCandidacyRequest(term);
    }
}
