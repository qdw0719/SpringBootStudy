package com.example.oauth2.repository;

import com.example.oauth2.domain.SnsOAuth2User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsOAuth2UserRepository extends JpaRepository<SnsOAuth2User, String> {
}
