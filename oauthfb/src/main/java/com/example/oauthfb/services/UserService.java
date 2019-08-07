package com.example.oauthfb.services;

import com.example.oauthfb.entity.User;
import com.example.oauthfb.interfaces.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    public boolean exists(String id) {
        return userRepository.findById(id).isPresent();
    }

    public void insertUser(User user) {
        this.userRepository.save(user);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
    public void checkPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        logger.info(token);
        Facebook fbApi = new FacebookTemplate(token);
        String[] fields = {"first_name", "last_name", "email", "name"};
        org.springframework.social.facebook.api.User fbUser = fbApi.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
        if (this.exists(fbUser.getId()))
            logger.info("this user exists");
        else {
            User user = new User(fbUser.getId(), fbUser.getName(), fbUser.getEmail());
            this.insertUser(user);
        }
    }

}
