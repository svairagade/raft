package com.cmpe.raft.consensus.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 06-12-2016.
 */
@XmlRootElement
public class ClientRequest {
    private String type;
    private Person person;

    public ClientRequest() {
    }

    public ClientRequest(String type, Person person) {
        this.type = type;
        this.person = person;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
