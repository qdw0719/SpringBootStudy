package com.example.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SnsLoginSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private SnsOAuth2UserService oAuth2UserService;
//
//    @Autowired
//    private SnsOidcUserService oidcUserService;

    @Autowired
    private SnsOAuth2SuccessHandler successHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2Login(oauth2 -> oauth2
//                            .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService)
//                                        .oidcUserService(oidcUserService))
                            .successHandler(successHandler)
            );
    }

}
