package com.example.batch_practice.repository;

import com.example.batch_practice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findAllByUpdatedDate(LocalDate date);
}
