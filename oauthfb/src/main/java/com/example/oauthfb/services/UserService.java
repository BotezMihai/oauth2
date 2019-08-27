package com.example.oauthfb.services;

import com.example.oauthfb.accesstoken.AccessToken;
import com.example.oauthfb.accesstoken.AccessTokenData;
import com.example.oauthfb.accesstoken.Data;
import com.example.oauthfb.entity.Counter;
import com.example.oauthfb.entity.TokenTable;
import com.example.oauthfb.entity.UserDetails;

import com.example.oauthfb.controllers.FacebookController;
import com.example.oauthfb.interfaces.TokenRepository;
import com.example.oauthfb.interfaces.UserRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;


import static org.springframework.data.mongodb.core.query.Query.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MongoOperations mongo;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private FacebookController facebookController;

    public boolean exists(String id) {
        return userRepository.findById(id).isPresent();
    }

    public boolean existsToken(String token) {
        return tokenRepository.existsTokenTableByAccessToken(token);
    }

    public void insertUser(UserDetails user) {
        this.userRepository.save(user);
    }

    public String getUser(String id) {

        UserDetails userDetails=this.userRepository.findById(id).get();
        return userDetails.getEmail();
    }

    public String getUserId(String token) {

        TokenTable tokenTable=this.tokenRepository.findTokenTableByAccessToken(token);
        return tokenTable.getUserId();
    }

    public void insertToken(TokenTable tokenTable) {
        this.tokenRepository.save(tokenTable);
        LOGGER.info("token saved succesfully");
    }

    public Iterable<UserDetails> getAllUsers() {
        return userRepository.findAll();
    }

    public String genCSRF() {
        return UUID.randomUUID().toString();
    }

    public boolean userIsAuthenticated(String access_token) {
        AccessTokenData accessTokenData;
        try {
            accessTokenData = inspectAccessToken(access_token, getAppAccessToken());
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }

        return !(!accessTokenData.isIs_valid() || accessTokenData.getApp_id() != Long.valueOf(facebookController.APP_ID));
    }

    public AccessToken getAccessTokenFromCode(String code) {
        Map<String, String> urlparams = new HashMap<>();
        urlparams.put("client_id", facebookController.APP_ID);
        urlparams.put("redirect_uri", facebookController.REDIRECT_URI);
        urlparams.put("client_secret", facebookController.APP_SECRET);
        urlparams.put("code", code);

        try {
            return restTemplate.getForObject(
                    "https://graph.facebook.com/oauth/access_token?client_id={client_id}&code={code}&client_secret"
                            + "={client_secret}&redirect_uri={redirect_uri}",
                    AccessToken.class, urlparams);
        } catch (HttpStatusCodeException exception) {
            LOGGER.warn(exception.getResponseBodyAsString());
            throw new RuntimeException(String.valueOf(exception.getStatusCode()));
        }
    }

    public String getAppAccessToken() {
        Map<String, String> urlparams = new HashMap<>();
        urlparams.put("client_id", facebookController.APP_ID);
        urlparams.put("client_secret", facebookController.APP_SECRET);
        LOGGER.info("Retrieving app access token");

        try {
            String json = restTemplate.getForObject(
                    "https://graph.facebook.com/oauth/access_token?client_id={client_id}&client_secret={client_secret"
                            + "}&grant_type=client_credentials",
                    String.class, urlparams);
            return new JSONObject(json).getString("access_token");
        } catch (HttpStatusCodeException exception) {
            LOGGER.warn(exception.getResponseBodyAsString());
            throw new RuntimeException(String.valueOf(exception.getStatusCode()));
        }
    }

    public AccessTokenData inspectAccessToken(String accessToken, String appAccessToken) {
        Map<String, String> urlparams = new HashMap<>();
        urlparams.put("input_token", accessToken);
        urlparams.put("access_token", appAccessToken);
        try {
            return restTemplate.getForObject(
                    "https://graph.facebook.com/debug_token?input_token={input_token}&access_token={access_token}",
                    Data.class, urlparams).getData();
        } catch (HttpStatusCodeException exception) {
            LOGGER.warn(exception.getResponseBodyAsString());
            throw new RuntimeException(String.valueOf(exception.getStatusCode()));
        }
    }

    public UserDetails getUserDetailsFromAccessToken(String accessToken) {

        Map<String, String> urlparams = new HashMap<>();
        urlparams.put("access_token", accessToken);
        urlparams.put("fields", "id,name,email");
        LOGGER.info("Retrieving user details with {} and {}", accessToken, urlparams);
        try {
            return restTemplate
                    .getForObject("https://graph.facebook.com/v2.9/me/?access_token={access_token}&fields={fields}",
                            UserDetails.class, urlparams);
        } catch (HttpStatusCodeException exception) {
            LOGGER.warn(exception.getResponseBodyAsString());
            throw new RuntimeException(String.valueOf(exception.getStatusCode()));
        }
    }

    public int getNextSequence(String collectionName) {

        Counter counter = mongo.findAndModify(
                query(where("_id").is(collectionName)),
                new Update().inc("seq", 1),
                options().returnNew(true),
                Counter.class);
        return counter.getSeq();


    }
}