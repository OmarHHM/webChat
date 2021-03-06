package com.web.chat.payload.impl;


import com.web.chat.payload.Payload;


/**
* Represent the payload of Users disconnected
*/
public class DisconnectedUser implements Payload {

    public static final String TYPE = "disconnectedUser";

    private String username;

    public DisconnectedUser() {

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
