package com.example.oauthfb.unittests;

import com.example.oauthfb.OauthfbApplication;
import com.example.oauthfb.UserDetails;
import com.example.oauthfb.controllers.FacebookController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;


import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.oauthfb.services.UserService;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;


import javax.servlet.http.Cookie;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = OauthfbApplication.class)
@WebMvcTest(FacebookController.class)

public class OauthSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private UserService us;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenAccessASecuredEndpointReturn401() {
        try {
            mvc.perform(get("/facebook/userinfo"))
                    .andExpect(status().isUnauthorized());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenAcessFacebookAuthShouldReturnFalse() {

        try {
            mvc.perform(get("/facebook/auth")).andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenAcessFacebookGetLoginUriReturnUri() {
        given(userService.genCSRF()).willReturn("dsfdsfsdfdsfsdf");
        try {
            mvc.perform(get("/facebook/getLoginUri")).andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void givenCookieShouldResponseWith200(){
        Cookie cookie=new Cookie("access_token","EAAJ4ncZC1MngBACKyA473ZChGtZCHv9YNQUphZAk6CGZBzXBlp5FIKh3gaho1uHmSXGton0eX9LSjmOaRYXC33Q6IwFBvAikrUWW6GUKOksZB3jKTnSxtkpqpNvEdlFbtmZAOBvaFS3Q5IEPEKMUPlgfIlophQtWweX7ZBqsXFk4YwZDZD");
        try {
       mvc.perform(get("/facebook/userinfo").cookie(cookie).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andDo(print());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
