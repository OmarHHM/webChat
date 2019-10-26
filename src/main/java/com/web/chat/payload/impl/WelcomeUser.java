package com.web.chat.payload.impl;


import com.web.chat.payload.Payload;


/**
* Represent the payload for the welcome of the users
*/

public class WelcomeUser implements Payload {

    public static final String TYPE = "welcomeUser";

    private String username;

    public WelcomeUser() {

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
