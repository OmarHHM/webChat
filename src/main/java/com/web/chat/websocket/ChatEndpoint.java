package com.web.chat.websocket;


import javax.enterprise.context.Dependent;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.web.chat.payload.WebSocketMessage;
import com.web.chat.payload.impl.*;
import com.web.chat.websocket.codec.MessageDecoder;
import com.web.chat.websocket.codec.MessageEncoder;
import com.web.chat.websocket.config.CDIConfiguration;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Dependent
@ServerEndpoint(
        value = "/chat",
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class,
        configurator = CDIConfiguration.class)
public class ChatEndpoint {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        String username = session.getUserPrincipal().getName();
        welcomeUser(session, username);
        userConnected(session, username);
        availableUsers();
    }

    @OnMessage
    public void onMessage(Session session, WebSocketMessage message) {
        if (message.getPayload() instanceof SendTextMessagePayload) {
            SendTextMessagePayload payload = (SendTextMessagePayload) message.getPayload();
            textMessage(session.getUserPrincipal().getName(), payload.getContent());
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        String username = session.getUserPrincipal().getName();
        userDisconnected(username);
        availableUsers();
    }

    private void welcomeUser(Session currentSession, String username) {
        WelcomeUser payload = new WelcomeUser();
        payload.setUsername(username);
        currentSession.getAsyncRemote().sendObject(new WebSocketMessage(payload));
    }

    private void userConnected(Session currentSession, String username) {
        ConnectedUser payload = new ConnectedUser();
        payload.setUsername(username);
        broadcast(currentSession, new WebSocketMessage(payload));
    }

    private void userDisconnected(String username) {
        DisconnectedUser payload = new DisconnectedUser();
        payload.setUsername(username);
        broadcast(new WebSocketMessage(payload));
    }

    private void textMessage(String username, String text) {
        TextMessage payload = new TextMessage();
        payload.setContent(text);
        payload.setUsername(username);
        broadcast(new WebSocketMessage(payload));
    }

    private void availableUsers() {

        Set<String> usernames = sessions.stream()
                .map(Session::getUserPrincipal)
                .map(Principal::getName)
                .distinct()
                .collect(Collectors.toSet());

        AvailableUsers payload = new AvailableUsers();
        payload.setUsernames(usernames);
        broadcast(new WebSocketMessage(payload));
    }

    private void broadcast(WebSocketMessage message) {
        synchronized (sessions) {
            sessions.forEach(session -> {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendObject(message);
                }
            });
        }
    }

    private void broadcast(Session ignoredSession, WebSocketMessage message) {
        synchronized (sessions) {
            sessions.forEach(session -> {
                if (session.isOpen() && !session.getId().equals(ignoredSession.getId())) {
                    session.getAsyncRemote().sendObject(message);
                }
            });
        }
    }
}
