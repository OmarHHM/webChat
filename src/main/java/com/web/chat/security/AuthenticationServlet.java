package com.web.chat.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Class for autenticate it´s retrieve a token if credentials are corrects
 * 
 * */
@WebServlet("/auth")
public class AuthenticationServlet extends HttpServlet {

    @Inject
    private ObjectMapper mapper;

    @Inject
    private Authenticator authenticator;


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Credentials credentials = mapper.readValue(req.getReader(), Credentials.class);

        if (authenticator.checkCredentials(credentials.getUsername(), credentials.getPassword())) {
            String token = authenticator.issueAccessToken(credentials.getUsername());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            mapper.writeValue(resp.getWriter(), new WebSocketAccessToken(token));
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("text/plain");
            resp.getWriter().write("Invalid credentials");
        }
    }

  
    private static class Credentials {

        private String username;

        private String password;

        public Credentials() {

        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * Model that holds an access token for the WebSocket endpoints.
     */
    private static class WebSocketAccessToken {

        private String token;

        public WebSocketAccessToken() {

        }

        public WebSocketAccessToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}