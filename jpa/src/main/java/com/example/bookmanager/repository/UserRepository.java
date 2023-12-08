package com.example.bookmanager.repository;

import com.example.bookmanager.domain.User;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

//@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByName(String name);

    List<User> findFirstByName(String name, Sort sort);
    List<User> findTop1ByName(String name);
    List<User> findTop1ByNameOrderByIdDesc(String name);

    List<User> findByNameAndEmail(String name, String email);
    List<User> findByNameOrEmail(String name, String email);

    List<User> findByCreatedAtAfter(LocalDateTime localDateTime);
    List<User> findByCreatedAtBefore(LocalDateTime localDateTime);

    List<User> findByIdBetween(Long Id1, Long Id2);

    List<User> findByUpdatedAtIsNotNull();

    List<User> findByNameIn(List<String> names);
    List<User> findByNameNotIn(List<String> names);

    List<User> findByEmailContains(String email);

    @Query(value = "select * from user limit 1", nativeQuery = true)
    Map<String, Object> getRow();
}
