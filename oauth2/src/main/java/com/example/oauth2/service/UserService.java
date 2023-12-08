package com.example.oauth2.service;

import com.example.oauth2.domain.SnsOAuth2User;
import com.example.oauth2.domain.UserAuthority;
import com.example.oauth2.domain.User;
import com.example.oauth2.repository.SnsOAuth2UserRepository;
import com.example.oauth2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SnsOAuth2UserRepository oAuth2UserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username);
    }

    public User findUser(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void addAuthority(Long userId, String authority){
        userRepository.findById(userId).ifPresent(user->{
            UserAuthority newRole = new UserAuthority(user.getUserId(), authority);
            if(user.getAuthorities() == null){
                HashSet<UserAuthority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            }else if(!user.getAuthorities().contains(newRole)){
                HashSet<UserAuthority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities());
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            }
        });
    }

    public void removeAuthority(Long userId, String authority){
        userRepository.findById(userId).ifPresent(user->{
            if(user.getAuthorities()==null) return;
            UserAuthority targetRole = new UserAuthority(user.getUserId(), authority);
            if(user.getAuthorities().contains(targetRole)){
                user.setAuthorities(
                        user.getAuthorities().stream().filter(auth->!auth.equals(targetRole))
                            .collect(Collectors.toSet())
                );
                save(user);
            }
        });
    }

    public User loadUser(final SnsOAuth2User oAuth2User){
        SnsOAuth2User user = oAuth2UserRepository.findById(oAuth2User.getOauth2UserId()).orElseGet(()->{
            User User = new User();
            User.setEmail(oAuth2User.getEmail());
            User.setEnabled(true);
            User.setPassword("");
            userRepository.save(User);
            addAuthority(User.getUserId(), "ROLE_USER");
            oAuth2User.setUserId(User.getUserId());
            oAuth2User.setCreatedAt(LocalDateTime.now());
            return oAuth2UserRepository.save(oAuth2User);
        });
        return userRepository.findById(user.getUserId()).get();
    }

}
