package com.web.chat.payload.impl;


import com.web.chat.payload.Payload;

/**
* Represent the payload of Users connected
*/
public class ConnectedUser implements Payload {

    public static final String TYPE = "connectedUser";

    private String username;

    public ConnectedUser() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
