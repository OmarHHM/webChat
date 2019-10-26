package com.web.chat.payload.impl;


import java.util.Set;

import com.web.chat.payload.Payload;

/**
 * Represent the payload of Users available
 * */
public class AvailableUsers implements Payload {

    public static final String TYPE = "availableUsers";

    private Set<String> usernames;

    public AvailableUsers() {

    }

    public Set<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(Set<String> usernames) {
        this.usernames = usernames;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
