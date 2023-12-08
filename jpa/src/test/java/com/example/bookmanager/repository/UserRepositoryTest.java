package com.example.bookmanager.repository;

import com.example.bookmanager.domain.Gender;
import com.example.bookmanager.domain.User;
import com.example.bookmanager.domain.UserHistory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
//import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;
//import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Test
    @Transactional
    void crudTest() {
        List<User> userFindAll = userRepository.findAll(Sort.by(Direction.DESC, "createdAt"));
        userFindAll.forEach(row -> System.out.println("findAll() => " + row));

        List<User> userFindAllById = userRepository.findAllById(Lists.newArrayList(1L, 3L, 5L));
        userFindAllById.forEach(row -> System.out.println("findAllById() => " + row));

        User user1 = new User().builder()
                .name("pyj")
                .email("qdw0719@naver.com")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(null)
                .build();
        User user2 = new User().builder()
                .name("yjp")
                .email("isMe@google.com")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(null)
                .build();
        userRepository.saveAll(Lists.newArrayList(user1, user2));

        List<User> userSaveAfterFindAll = userRepository.findAll(Sort.by(Direction.DESC, "createdAt"));
        userSaveAfterFindAll.forEach(row -> System.out.println("saveAfterFindAll() => " + row));

        User userGetOne = userRepository.getOne(1L);
        User userFindById = userRepository.findById(5L).orElse(null);
        System.out.println("userGetOne => " + userGetOne);
        System.out.println("userFindById => " + userFindById);

        User user3 = new User().builder()
                .name("ppp")
                .email("pyj@naver.com")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(null)
                .build();
        userRepository.save(user3);
        userRepository.flush();
        userRepository.findAll(Sort.by(Direction.DESC, "createdAt")).forEach(System.out::println);

        long count = userRepository.count();
        System.out.println("count => " + count);

        boolean exists = userRepository.existsById(1L);
        System.out.println("exists => " + exists);

//        userRepository.delete(userRepository.findById(2L).orElseThrow(() -> new RuntimeException()));
//        userRepository.deleteAll(userRepository.findAllById(Lists.newArrayList(3L, 4L)));
//        userRepository.deleteById(5L);
//        userRepository.deleteInBatch(userRepository.findAllById(Lists.newArrayList(6L, 7L)));
//        userRepository.findAll().forEach(row -> System.out.println("after delete => " + row));

        Page<User> userPage = userRepository.findAll(PageRequest.of(0, 5, Sort.by(Direction.DESC, "createdAt")));
        System.out.println("after paging => " + userPage);
        System.out.println("total elements => " + userPage.getTotalElements());
        System.out.println("total pages => " + userPage.getTotalPages());
        System.out.println("number of elements => " + userPage.getNumberOfElements());
        System.out.println("sort : " + userPage.getSort());
        System.out.println("size : " + userPage.getSize());
        userPage.getContent().forEach(System.out::println);

        User user4 = new User().builder()
                .name("PYJ")
                .email("google")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(null)
                .build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("name")
                .withMatcher("email", contains()); // startsWith(), endsWith()
        Example<User> example = Example.of(user4, matcher);
        userRepository.findAll(example).forEach(row -> System.out.println("matcher => " + row));

//        User user5 = new User().builder()
//                .name("new pyj")
//                .email("qdw0719@google.com")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(null)
//                .build();
        User findUser1 = userRepository.findById(1L).orElseThrow(() -> new RuntimeException());
        findUser1.setEmail("qdw0719_updated@google.com");
//        findUser1.setUpdatedAt(LocalDateTime.now());
        userRepository.save(findUser1);

        userRepository.findAll().forEach(row -> System.out.println("after user1 updated => " + row));
    }

    @Test
    void selectTest() {
        List<User> findByName = userRepository.findByName("Park");
        System.out.println("findByName => " + findByName);

//        List<User> findFirstByName = userRepository.findFirstByName("Park");
//        System.out.println("findFirstByName => " + findFirstByName);

        List<User> findTop1ByName = userRepository.findTop1ByName("Park");
        System.out.println("findTop1ByName => " + findTop1ByName);

        List<User> findByNameAndEmail = userRepository.findByNameAndEmail("Park", "park@google.com");
        System.out.println("findByNameAndEmail => " + findByNameAndEmail);

        List<User> findByNameOrEmail = userRepository.findByNameOrEmail("Park", "kim@naver.com");
        System.out.println("findByNameOrEmail => " + findByNameOrEmail);

        List<User> findByCreatedAtAfter = userRepository.findByCreatedAtAfter(LocalDateTime.now());
        System.out.println("findByCreatedAtAfter => " + findByCreatedAtAfter);

        List<User> findByCreatedAtBefore = userRepository.findByCreatedAtBefore(LocalDateTime.now());
        System.out.println("findByCreatedAtBefore => " + findByCreatedAtBefore);

        List<User> findByIdBetween = userRepository.findByIdBetween(2L, 4L);
        System.out.println("findByIdBetween => " + findByIdBetween);

        List<User> findByUpdatedAtIsNotNull = userRepository.findByUpdatedAtIsNotNull();
        System.out.println("findByUpdatedAtIsNotNull => " + findByUpdatedAtIsNotNull);

        List<User> findByNameIn = userRepository.findByNameIn(Lists.newArrayList("Park", "Lee"));
        System.out.println("findByNameIn => " + findByNameIn);

        List<User> findByNameNotIn = userRepository.findByNameNotIn(Lists.newArrayList("Park", "Lee"));
        System.out.println("findByNameNotIn => " + findByNameNotIn);

        List<User> findByEmailContains = userRepository.findByEmailContains("google");
        System.out.println("findByEmailContains => " + findByEmailContains);
    }

    @Test
    void sortTest() {
        List<User> findTop1ByName = userRepository.findTop1ByName("Park");
        System.out.println("findTop1ByName => " + findTop1ByName);

        List<User> findTop1ByNameOrderByIdDesc = userRepository.findTop1ByNameOrderByIdDesc("Park");
        System.out.println("findTop1ByNameOrderByIdDesc => " + findTop1ByNameOrderByIdDesc);

        List<User> findFirstByNameWithSortParams = userRepository.findFirstByName("Park", Sort.by(Order.desc("id")));
        System.out.println("findFirstByNameWithSortParams => " + findFirstByNameWithSortParams);
    }

    @Test
    void insertAndUpdateTest() {
        User user1 = new User().builder()
                .name("Park Young Jun")
                .email("qdw0719@naver.com")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(null)
                .build();
        userRepository.save(user1);

        User user2 = userRepository.findById((long) userRepository.findAll().size()).orElseThrow(() -> new RuntimeException());
        user2.setName("Park YJ");
        userRepository.save(user2);

        userRepository.findAll().forEach(row -> System.out.println("insert and update => " + row));
    }

    @Test
    void enumGenderTest() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getId() % 2 == 0) {
                user.setGender(Gender.MALE);
            } else {
                user.setGender(Gender.FEMAIL);
            }
            userRepository.save(user);
        }

        userRepository.findAll().forEach(System.out::println);
        System.out.println(userRepository.getRow().get("gender"));
    }

    @Test
    void prePersistTest() {
        User user = new User().builder()
                .name("PYJ")
                .email("qkrdudwns@google.com")
                .build();
        userRepository.save(user);
        userRepository.findAll().forEach(System.out::println);
    }

    @Test
    void userRelationTest() {
        User user = new User().builder()
                .userId(1L)
                .name("박영준")
                .email("qdw0719@google.com")
                .build();
        userRepository.save(user);
        userRepository.findAll().forEach(row -> System.out.println("user => " + row));
        userHistoryRepository.findAll().forEach(row -> System.out.println("user history => " + row));

        user.setGender(Gender.MALE);
        userRepository.save(user);
        userRepository.findAll().forEach(row -> System.out.println("user => " + row));
        userHistoryRepository.findAll().forEach(row -> System.out.println("user history => " + row));

        user.setEmail("pyj@google.com");
        userRepository.save(user);
        userRepository.findAll().forEach(row -> System.out.println("user => " + row));
        userHistoryRepository.findAll().forEach(row -> System.out.println("user history => " + row));
    }
}