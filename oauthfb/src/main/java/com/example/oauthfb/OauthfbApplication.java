package com.example.oauthfb;

import com.example.oauthfb.config.SpringSecurityConfig;
import com.example.oauthfb.entity.User;
import com.example.oauthfb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.logging.Logger;

@SpringBootApplication
@EnableOAuth2Sso
@RestController
public class OauthfbApplication {
    @Autowired
    private UserService userService;
    @Autowired
    private SpringSecurityConfig springSecurityConfig;
   // Logger logger = Logger.getLogger("OauthfbApplication.class");

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        Facebook fbApi = new FacebookTemplate(token);
        String[] fields = {"first_name", "last_name", "email", "name"};
        org.springframework.social.facebook.api.User fbUser = fbApi.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
        if (userService.exists(fbUser.getId()))
            // logger.log("exista");
            System.out.println("exista");
        else {
            User user = new User(fbUser.getId(), fbUser.getName(), fbUser.getEmail());
            userService.insertUser(user);
        }
        return principal;
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .antMatcher("/**")
//                .authorizeRequests()
//                .antMatchers("/", "/login**", "/webjars/**", "/error**")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and().logout().logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll().and().csrf().disable();
//
//    }

    public static void main(String[] args) {
        SpringApplication.run(OauthfbApplication.class, args);
    }


}
