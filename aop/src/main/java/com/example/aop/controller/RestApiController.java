package com.example.aop.controller;

import com.example.aop.annotation.Decode;
import com.example.aop.annotation.Timer;
import com.example.aop.dto.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @GetMapping("/get/{id}")
    public String get(@PathVariable Long id, @RequestParam String name) {
        return id + " " + name;
    }

    @PostMapping("/set")
    public User post(@RequestBody User user) {
        return user;
    }

    @PostMapping("/put")
    @Decode
    public User put(@RequestBody User user) {
        System.out.println("put Method : " + user);
        return user;
    }

    @PostMapping("/delete")
    @Timer
    public void delete() throws InterruptedException {
        Thread.sleep(1000 * 2);
    }
}
