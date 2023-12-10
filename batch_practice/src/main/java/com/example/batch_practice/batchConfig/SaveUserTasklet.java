package com.example.batch_practice.batchConfig;

import com.example.batch_practice.model.User;
import com.example.batch_practice.model.User.Level;
import com.example.batch_practice.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaveUserTasklet implements Tasklet {

    private final UserRepository userRepository;

    public SaveUserTasklet(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<User> users = userRepository.findAll();
        if (users.size() == 0) {
            users = createUsers();
            Collections.shuffle(users);
            userRepository.saveAll(users);
        }
        return RepeatStatus.FINISHED;
    }

    public int randomNumber() {
        return (int) (Math.random() * 5);
    }

    private List<User> createUsers() {
        String[] names = {"Park", "Kim", "Choi", "Lee", "Hwang"};
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(User.builder()
                    .totalAmount(1000)
                    .userName(names[randomNumber()])
                    .level(Level.NORMAL)
                    .createdDate(LocalDateTime.now())
                    .build());
        }

        for (int i = 10; i < 20; i++) {
            users.add(User.builder()
                    .totalAmount(200000)
                    .userName(names[randomNumber()])
                    .level(Level.NORMAL)
                    .createdDate(LocalDateTime.now())
                    .build());
        }

        for (int i = 20; i < 30; i++) {
            users.add(User.builder()
                    .totalAmount(300000)
                    .userName(names[randomNumber()])
                    .level(Level.NORMAL)
                    .createdDate(LocalDateTime.now())
                    .build());
        }

        for (int i = 30; i < 40; i++) {
            users.add(User.builder()
                    .totalAmount(500000)
                    .userName(names[randomNumber()])
                    .level(Level.NORMAL)
                    .createdDate(LocalDateTime.now())
                    .build());
        }
        return users;
    }
}
