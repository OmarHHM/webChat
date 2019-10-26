package com.web.chat.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
/**
 * Class of definition of credentials 
 * 
 * */
@ApplicationScoped
public class Authenticator {

    private SecureRandom random;

    private Cache<String, String> accessTokens;

    private static Map<String, String> users = new HashMap<>();

    static {
        users.put("Juan", "123");
        users.put("Admin", "123");
        users.put("Invitado", "123");
        users.put("Ana", "123");
    }

    @PostConstruct
    public void init() {
        random = new SecureRandom();
        accessTokens = CacheBuilder.newBuilder()
                .expireAfterAccess(15, TimeUnit.SECONDS) 
                .build();
    }

    public boolean checkCredentials(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public String issueAccessToken(String username) {
        String accessToken = generateRandomString();
        accessTokens.put(accessToken, username);
        return accessToken;
    }

    public Optional<String> getUsernameFromToken(String accessToken) {
        String username = accessTokens.getIfPresent(accessToken);
        if (username == null) {
            return Optional.empty();
        } else {
            accessTokens.invalidate(accessToken); 
            return Optional.of(username);
        }
    }

    private String generateRandomString() {
        return new BigInteger(130, random).toString(32);
    }
}
