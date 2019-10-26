package com.web.chat.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * interface representing the type of payload received 
 * **/
public interface Payload {

    @JsonIgnore
    String getType();
}
