package com.example.oauthfb.unittests;


import com.example.oauthfb.controllers.FacebookController;
import org.apache.http.conn.ssl.NoopHostnameVerifier;

import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClients;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = FacebookController.class)
@WebMvcTest(FacebookController.class)
public class OauthSecurityTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void whenAccessASecuredEndpointReturn401() {
        try {
            mvc.perform(get("/facebook/userinfo"))
                    .andExpect(status().isUnauthorized());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
