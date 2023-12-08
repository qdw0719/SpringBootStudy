package com.example.bookmanager.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ImsiController {

    @PostMapping("/test")
    public String test() {
        return "test success";
    }
}
