package com.web.chat.payload;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.web.chat.payload.impl.*;

/**
 * Represent the type of payload received 
 * */
public class WebSocketMessage {

    private String type;

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = AvailableUsers.class, name = AvailableUsers.TYPE),
            @JsonSubTypes.Type(value = ConnectedUser.class, name = ConnectedUser.TYPE),
            @JsonSubTypes.Type(value = DisconnectedUser.class, name = DisconnectedUser.TYPE),
            @JsonSubTypes.Type(value = TextMessage.class, name = TextMessage.TYPE),
            @JsonSubTypes.Type(value = SendTextMessagePayload.class, name = SendTextMessagePayload.TYPE),
            @JsonSubTypes.Type(value = WelcomeUser.class, name = WelcomeUser.TYPE)
    })
    private Payload payload;

    public WebSocketMessage() {

    }

    public WebSocketMessage(Payload payload) {
        this.type = payload.getType();
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
