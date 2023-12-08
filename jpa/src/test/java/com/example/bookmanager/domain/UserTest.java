package com.example.bookmanager.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void test() {
        User user = new User();
        user.setEmail("qdw0719@naver.com");
        user.setName("박영준");

        System.out.println("user : " + user);
    }
}