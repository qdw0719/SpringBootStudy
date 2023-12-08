package com.example.spring_ioc;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class Base64Encoder implements IEncoder {

    @Override
    public String encode(String message) {
        return Base64.getEncoder().encodeToString(message.getBytes());
    }
}
