package com.example.oauthfb.unittests;


import org.apache.http.client.ClientProtocolException;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.test.context.junit4.SpringRunner;


import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

import org.springframework.web.client.RestTemplate;


import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SuppressWarnings("SpringJavaAutowiringInspection")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OauthSecurityTest {

    @Autowired
    private TestRestTemplate template;

//    @Test
//    public void givenAuthRequestOnPrivateService_shouldReturn401() throws Exception {
//        ResponseEntity<String> result = template
//                .getForEntity("https://localhost:8445/facebook/userinfo", String.class);
//        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
//    }

    @Test
    public final void givenAcceptingAllCertificates_whenHttpsUrlIsConsumed_thenOk()
            throws GeneralSecurityException {
        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
 
        ResponseEntity<String> response
                = new RestTemplate(requestFactory).exchange(
                "https://localhost:8445/index.html" , HttpMethod.GET, null, String.class);
        assertThat(response.getStatusCode().value(), equalTo(200));

    }
}
