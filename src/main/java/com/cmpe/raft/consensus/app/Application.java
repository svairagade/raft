package com.cmpe.raft.consensus.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.cmpe.raft.consensus.node.Node;
import org.glassfish.grizzly.PortRange;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Sushant on 26-11-2016.
 */
public class Application {

    @Parameter(names = {"--ipaddress", "-ip"}, description = "IP address of the node")
    private static String ip = "0.0.0.0";
    @Parameter(names = {"--port", "-p"}, description = "Port of the node")
    private static Integer port = 8080;
    @Parameter(names = {"--dockerised", "-do"}, description = "Is the app dockerised")
    private static boolean dockerised = false;
    private static String uri = "http://%s:%d/raft/";


    private static HttpServer startServer() {
        uri = String.format(uri, ip, port);
        System.out.println(uri);
        if (!dockerised) {
            final ResourceConfig rc = new ResourceConfig().packages("com.cmpe.raft.consensus");
            return GrizzlyHttpServerFactory.createHttpServer(URI.create(uri), rc);
        } else {
            HttpServer server = new HttpServer();
            NetworkListener listener = new NetworkListener("grizzly", ip, new PortRange(port));
            server.addListener(listener);
            return server;
        }
    }

    public static void main(String[] args) throws IOException {
        Application application = new Application();
        new JCommander(application, args);
        HttpServer server = null;
        try {
            server = startServer();
            Node.getInstance();     //Just to create an instance and initialise
            //new HeartBeatJob().sendHeartBeat();
            System.out.println(String.format("Jersey app started with WADL available at "
                    + "%sapplication.wadl\nHit enter to stop it...", uri));

            System.in.read();
        } catch (IOException io) {
            //TODO log
        } finally {
            if (server != null) {
                server.shutdownNow();
            }
        }
    }

    public static String getIp() {
        return ip;
    }

    public static Integer getPort() {
        return port;
    }
}
