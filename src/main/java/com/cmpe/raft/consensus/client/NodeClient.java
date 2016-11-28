package com.cmpe.raft.consensus.client;

import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.Node;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Sushant on 27-11-2016.
 */
public class NodeClient {

    private String host;
    private Integer port;
    private CloseableHttpClient client = null;
    private String apiURL = null;

    public NodeClient(String host, Integer port) {
        this.host = host;
        this.port = port;
        initialize();
    }

    private void initialize() {
        client = HttpClients.createDefault();
        apiURL = "http://" + host + ":" + port + "/raft/node/%s?ip="+ Application.getIp() +"&port="+Application.getPort()+"&term=%d"; // Using sample instead of g
    }

    public HeartBeat sendHeartBeat() {
        String getHttpUri = String.format(apiURL, "heartbeat", Node.getInstance().getTerm());
        System.out.println(NodeClient.class.getCanonicalName()+" Get URI "+ getHttpUri);
        HttpGet httpGet = new HttpGet(getHttpUri);
        httpGet.setHeader("Accept", "application/json");
        HeartBeat heartBeat = null;
        try {
            HttpResponse httpResponse = client.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            JSONObject jsonContent = new JSONObject(content);
            ObjectMapper mapper = new ObjectMapper();
            heartBeat = mapper.readValue(jsonContent.toString(), HeartBeat.class);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return heartBeat;
    }

    public Vote sendCandidacyRequest() {
        String getHttpUri = String.format(apiURL, "leader", Node.getInstance().getTerm());
        System.out.println(NodeClient.class.getCanonicalName()+" Get URI "+ getHttpUri);
        HttpGet httpGet = new HttpGet(getHttpUri);
        httpGet.setHeader("Accept", "application/json");
        Vote vote = null;
        try {
            HttpResponse httpResponse = client.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            System.out.println(NodeClient.class.getCanonicalName() + " Candidacy response "+ content);
            EntityUtils.consume(httpEntity);
            JSONObject jsonContent = new JSONObject(content);
            ObjectMapper mapper = new ObjectMapper();
            vote = mapper.readValue(jsonContent.toString(), Vote.class);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return vote;
    }

    public static void main(String[] args) {
        NodeClient nodeClient = new NodeClient("localhost", 8080);
        HeartBeat heartBeat = nodeClient.sendHeartBeat();
        System.out.println(NodeClient.class.getCanonicalName()+" Heart beat response: "+ heartBeat);
        nodeClient.sendCandidacyRequest();
    }
}
