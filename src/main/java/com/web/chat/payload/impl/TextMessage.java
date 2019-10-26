package com.web.chat.payload.impl;


import com.web.chat.payload.Payload;

/**
* Represent the payload of text message
*/
public class TextMessage implements Payload {

    public static final String TYPE = "textMessage";

    private String username;
    private String content;

    public TextMessage() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
