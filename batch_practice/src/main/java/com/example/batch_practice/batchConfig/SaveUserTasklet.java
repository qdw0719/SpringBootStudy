package com.example.batch_practice.batchConfig;

import com.example.batch_practice.model.Orders;
import com.example.batch_practice.model.User;
import com.example.batch_practice.model.User.Level;
import com.example.batch_practice.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaveUserTasklet implements Tasklet {

    private final int SIZE = 100;

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

    public int randomNameIndex() {
        return (int) (Math.random() * 5);
    }

    public int randomAmountIndex() {
        return (int) (Math.random() * 4);
    }

    private List<User> createUsers() {
        String[] names = {"Park", "Kim", "Choi", "Lee", "Hwang"};
        int[] amount = {1000, 200000, 300000, 500000};

        List<User> users = new ArrayList<>();
        for (int yearI = 2020, lastY = 2023; yearI <= lastY; yearI++) {
            for (int monthI = 1, lastM = 12; monthI <= lastM; monthI++) {
                LocalDate startDate = LocalDate.of(yearI, monthI, 1);
                LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
                    for (int i = 0; i < SIZE; i++) {
                        System.out.println(yearI + "-" + monthI + "-" + date.getDayOfMonth());
                        users.add(User.builder()
                                .orders(Collections.singletonList(
                                        Orders.builder()
                                                .amount(amount[randomAmountIndex()])
                                                .createdDate(LocalDate.of(yearI, monthI, date.getDayOfMonth()))
                                                .itemName("item" + i)
                                                .build()
                                ))
                                .userName(names[randomNameIndex()])
                                .level(Level.NORMAL)
                                .createdDate(LocalDateTime.now())
                                .build());
                    }
                }
            }
        }
        return users;
    }
}
