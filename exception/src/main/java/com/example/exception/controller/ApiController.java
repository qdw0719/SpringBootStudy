package com.example.exception.controller;

import com.example.exception.dto.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApiController {

    @PostMapping("/get")
    public User get(@RequestParam(required = false) String name, @RequestParam(required = false) Integer age) {
        User user = new User();
        user.setName(name);
        user.setAge(age);

        int a = 10 + age;

        System.out.println("/get user : " + user);
        return null;
    }

    @PostMapping("/set")
    public User set(@Valid @RequestBody User user) {

        System.out.println("/set user : " + user);
        return user;
    }

}
