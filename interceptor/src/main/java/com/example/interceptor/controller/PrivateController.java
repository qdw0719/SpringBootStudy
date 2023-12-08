package com.example.interceptor.controller;

import com.example.interceptor.annotation.Auth;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
@Auth
public class PrivateController {

    @PostMapping("/test")
    public String test() {
        return "private API Method";
    }
}
