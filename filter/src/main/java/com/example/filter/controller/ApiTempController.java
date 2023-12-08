package com.example.filter.controller;

import com.example.filter.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiTempController {

    @PostMapping("/temp")
    @ResponseBody
    public User setUser(@RequestBody User user) {
        log.info("Temp: {}, {}", user);
        return user;
    }
}
