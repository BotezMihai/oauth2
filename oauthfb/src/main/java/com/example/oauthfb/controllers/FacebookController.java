package com.example.oauthfb.controllers;

import com.example.oauthfb.accesstoken.AccessToken;
import com.example.oauthfb.accesstoken.AccessTokenData;

import com.example.oauthfb.entity.UserDetails;

import com.example.oauthfb.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@RestController
public class FacebookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookController.class);

    private RestTemplate restTemplate = new RestTemplate();
    public final String REDIRECT_URI;
    public final String APP_ID;
    public final String APP_SECRET;
    @Autowired
    private UserService userService;
    public FacebookController(
            @Value("${REDIRECT_URI}") String REDIRECT_URI,
            @Value("${APP_ID}") String APP_ID,
            @Value("${APP_SECRET}") String APP_SECRET) {
        this.REDIRECT_URI = REDIRECT_URI;
        this.APP_ID = APP_ID;
        this.APP_SECRET = APP_SECRET;
    }

    @GetMapping("/facebook/login")
    public ResponseEntity<?> facebookLogin(@RequestParam("code") String code, @RequestParam("state") String state,
                                           HttpServletResponse httpServletResponse) throws IOException {
        // Optional: Verify state (csrf) token

        AccessToken accessToken;
        try {
            accessToken = userService.getAccessTokenFromCode(code);
        } catch (RuntimeException e) {
            return ResponseEntity.status(Integer.parseInt(e.getMessage())).build();
        }

        LOGGER.info("Access token = {}", accessToken);

        String appAccessToken;
        try {
            appAccessToken = userService.getAppAccessToken();
        } catch (RuntimeException e) {
            return ResponseEntity.status(Integer.parseInt(e.getMessage())).build();
        }

        AccessTokenData accessTokenData = userService.inspectAccessToken(accessToken.getAccess_token(), appAccessToken);
        LOGGER.info("Verify token = {}", accessTokenData);
        if (!accessTokenData.isIs_valid() || accessTokenData.getApp_id() != Long.valueOf(APP_ID)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails;
        try {
            userDetails = userService.getUserDetailsFromAccessToken(accessToken.getAccess_token());
            if(userService.exists(userDetails.getId()))
            LOGGER.info("Userul exista cu id ul "+userDetails.getId());
            else
            {
                userService.insertUser(userDetails);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(Integer.parseInt(e.getMessage())).build();
        }

        LOGGER.info("User is authenticated: {}", userDetails);

        Cookie cookie = new Cookie("access_token", accessToken.getAccess_token());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge((int) accessToken.getExpires_in());
        httpServletResponse.addCookie(cookie);
        httpServletResponse.sendRedirect(REDIRECT_URI);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/facebook/auth")
    public boolean isAuthenticated(@CookieValue(value = "access_token", required = false) String access_token) {
        if (access_token == null) {
            return false;
        }
        return userService.userIsAuthenticated(access_token);
    }

    @GetMapping("/facebook/logout")
    public ResponseEntity logout(@CookieValue(value = "access_token") String access_token,
                                 HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie("access_token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/facebook/userinfo")
    public ResponseEntity<UserDetails> getUserDetails(@CookieValue(value = "access_token", required = false) String access_token) throws HttpClientErrorException {
        LOGGER.info("sunt in endpoint" + access_token);
        if (access_token == null) {
            UserDetails userDetails = null;
            LOGGER.info("TOKENUL ESTE NUL");
            return new ResponseEntity<UserDetails>(userDetails, HttpStatus.UNAUTHORIZED);

        }

        return new ResponseEntity<UserDetails>(userService.getUserDetailsFromAccessToken(access_token), HttpStatus.OK);

    }

    @GetMapping("/facebook/getLoginUri")
    public String getLoginUri() {

        String uri = "https://www.facebook.com/v2.9/dialog/oauth?client_id=" + APP_ID + "&redirect_uri=" + REDIRECT_URI
                + "&state=" + userService.genCSRF();
        return uri;
    }
    @GetMapping("/check-token")
    public String test(){

        return "test";

    }
}
