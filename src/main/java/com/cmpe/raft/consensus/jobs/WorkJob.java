package com.cmpe.raft.consensus.jobs;

import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.client.NodeClient;
import com.cmpe.raft.consensus.dao.Dao;
import com.cmpe.raft.consensus.model.ClientRequest;
import com.cmpe.raft.consensus.model.Person;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.client.Entity;

/**
 * Created by Sushant on 06-12-2016.
 */
public class WorkJob {
    private static final String QUEUE_HOST = "10.250.44.164";
    private static final String REQUEST_QUEUE = "raft_queue";
    private static final Integer REQUEST_PORT = 5672;
    private static Connection connection = null;
    private static Channel channel = null;
    private static boolean listen = false;

    public void listen() {
        try {
            listen = true;
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(QUEUE_HOST);
            factory.setPort(REQUEST_PORT);

            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(REQUEST_QUEUE, false, false, false, null);

            channel.basicQos(1);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(REQUEST_QUEUE, false, consumer);

            System.out.println(" [x] Awaiting RPC requests");

            while (listen) {
                String response = null;

                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                BasicProperties props = delivery.getProperties();
                BasicProperties replyProps = new BasicProperties.Builder().correlationId(props.getCorrelationId())
                        .build();

                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    ObjectMapper objectMapper = new ObjectMapper();
                    ClientRequest clientRequest = objectMapper.readValue(message, ClientRequest.class);
                    response = serveRequest(clientRequest);
                    performAction(clientRequest);
                } catch (Exception e) {
                    System.out.println(" [.] " + e.toString());
                    response = "";
                } finally {
                    if (response != null) {
                        channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignore) {
                }
            }
        }

    }


    public void stopListeing() {
        listen = false;
    }

    private String serveRequest(ClientRequest clientRequest) {
        Person person = null;
        switch (clientRequest.getAction()) {
            case "POST":
                person = Dao.addPerson(clientRequest.getPerson());
                break;
            case "PUT":
                person = Dao.updatePerson(clientRequest.getPerson());
                break;
            case "DELETE":
                person = Dao.deletePerson(clientRequest.getPerson().getId());
                break;
        }
        Entity<Person> entityPerson = Entity.json(person);
        return entityPerson.toString();
    }

    private void performAction(ClientRequest clientRequest) {
        if (!clientRequest.getAction().equals("GET")) {
            for (String host : Application.getClusterNodes().keySet()) {
                for (Integer port : Application.getClusterNodes().get(host)) {
                    NodeClient nodeClient = new NodeClient(host, port);
                    switch (clientRequest.getAction()) {
                        case "POST":
                            nodeClient.sendDoPostRequest(clientRequest.getPerson());
                            break;
                        case "PUT":
                            nodeClient.sendDoPutRequest(clientRequest.getPerson());
                            break;
                        case "DELETE":
                            nodeClient.sendDoDeleteRequest(clientRequest.getPerson().getId());
                            break;
                    }
                }
            }
        }
    }
}
