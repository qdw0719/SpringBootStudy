package com.example.oauth2.domain;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@IdClass(UserAuthority.class)
public class UserAuthority implements GrantedAuthority {

    @Id
    private Long userId;

    @Id
    private String authority;


}
