package com.cmpe.raft.consensus.util;

import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.node.Node;

import java.util.Date;

/**
 * Created by Sushant on 27-11-2016.
 */
public class ServiceUtil {

    public static HeartBeat constructHeartBeat(Node node) {
        HeartBeat heartBeat = new HeartBeat();
        heartBeat.setDate(new Date());
        heartBeat.setIp(Application.getIp());
        heartBeat.setPort(Application.getPort());
        heartBeat.setTerm(node.getTerm());
        return heartBeat;
    }
}
