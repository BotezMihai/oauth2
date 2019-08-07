package com.example.oauthfb.controllers;

import com.example.oauthfb.entity.User;
import com.example.oauthfb.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AuthentificationController {
    @Autowired
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(AuthentificationController.class);

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        userService.checkPrincipal();
        return principal;
    }

}
