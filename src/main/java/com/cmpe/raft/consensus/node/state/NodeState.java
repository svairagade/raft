package com.cmpe.raft.consensus.node.state;

import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Vote;

/**
 * Created by Sushant on 25-11-2016.
 */
public interface NodeState {

    void performTask();

    HeartBeat onHeartBeat(long term);

    Vote onCandidacyRequest(long term);
}