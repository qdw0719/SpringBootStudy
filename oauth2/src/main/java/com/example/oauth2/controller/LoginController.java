package com.example.oauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class LoginController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/snsLogin")
    public Object oAuthSnsLogin(@AuthenticationPrincipal Object user) {
        System.out.println("user => " + user);
        System.out.println(user.toString());
        return user;
    }

    @RequestMapping(value = "/successLogin", method = { RequestMethod.GET, RequestMethod.POST })
    public String successLogin(@RequestParam Object returnData) {
        System.out.println("successLogin => " + returnData.toString());
        return returnData.toString();
    }
}
