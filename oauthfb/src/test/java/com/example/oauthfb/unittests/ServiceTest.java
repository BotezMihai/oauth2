package com.example.oauthfb.unittests;

import com.example.oauthfb.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {

    @Mock
    private UserService userService;

    @Test
    public void whenCheckPrincipalCalledVerified(){

        doNothing().when(userService).checkPrincipal();
        userService.checkPrincipal();
        verify(userService,times(1)).checkPrincipal();

    }

}
