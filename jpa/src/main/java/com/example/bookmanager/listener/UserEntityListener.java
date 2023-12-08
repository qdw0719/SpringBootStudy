package com.example.bookmanager.listener;

import com.example.bookmanager.domain.User;
import com.example.bookmanager.domain.UserHistory;
import com.example.bookmanager.repository.UserHistoryRepository;
import com.example.bookmanager.support.BeanUtils;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

public class UserEntityListener {
    @PostPersist
    @PostUpdate
    public void prePersistAndUpdate(Object o) {
        UserHistoryRepository userHistoryRepository = BeanUtils.getBean(UserHistoryRepository.class);

        User user = (User) o;

        UserHistory userHistory = new UserHistory();
//        userHistory.setId(user.getId());
        userHistory.setUserId(user.getUserId());
        userHistory.setName(user.getName());
        userHistory.setEmail(user.getEmail());
        userHistory.setGender(user.getGender());

        userHistoryRepository.save(userHistory);
    }
}
