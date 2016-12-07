package com.cmpe.raft.consensus.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 06-12-2016.
 */
@XmlRootElement
public class ClientRequest {
    private String type;
    private Property property;

    public ClientRequest() {
    }

    public ClientRequest(String type, Property property) {
        this.type = type;
        this.property = property;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}
