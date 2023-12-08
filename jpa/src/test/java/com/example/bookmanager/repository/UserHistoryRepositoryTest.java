package com.example.bookmanager.repository;

import com.example.bookmanager.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserHistoryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Test
    void userHistoryTest() {
        User user = new User().builder()
                .name("PYJ")
                .email("qdw0719@google.com")
                .build();
        userRepository.save(user);
        
        user.setName("박영준");
        userRepository.save(user);

        userHistoryRepository.findAll().forEach(System.out::println);
    }
}