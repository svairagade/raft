package com.cmpe.raft.consensus.resource;

import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.error.Error;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.Node;
import com.cmpe.raft.consensus.util.StringUtil;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by Sushant on 26-11-2016.
 */
@Path("/node")
@Singleton                  // It is very important that this resource is kept Singleton
public class NodeResource {
    private Node node = Node.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/heartbeat")
    public Response heartBeat(@QueryParam("ip") String ip, @QueryParam(value = "port") Integer port, @QueryParam(value = "term") Long term) {
        if (StringUtil.isEmpty(ip) || port == null || term == null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Error.INVALID_REQUEST_MISSING_QUERY_PARAM)
                    .build();
        HeartBeat heartBeat = node.reactToHeartBeat(term);
        return Response.ok()
                .entity(heartBeat)
                .build();
    }


    @GET
    @Path("/leader")
    @Produces(MediaType.APPLICATION_JSON)
    public Response leaderRequest(@QueryParam("ip") String ip, @QueryParam(value = "port") Integer port, @QueryParam(value = "term") Long term) {
        if (StringUtil.isEmpty(ip) || port == null || term == null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Error.INVALID_REQUEST_MISSING_QUERY_PARAM)
                    .build();
        Vote vote = node.reactToLeaderRequest(term);
        return Response.ok()
                .entity(vote)
                .build();
    }
}
