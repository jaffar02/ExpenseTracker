package com.example.AuthService.service;

import com.example.AuthService.entities.UserInfo;
import com.example.AuthService.model.UserInfoDto;
import com.example.AuthService.repository.UserInfoRepository;
import com.example.AuthService.util.UserInfoInputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("Could not find any user with the given username");
        }
        return new CustomUserDetailsService(userInfo);
    }

    public UserInfo checkIfUserAlreadyExist(String username) {
        return userInfoRepository.findByUsername(username);
    }

    public Boolean signupUser(UserInfoDto userInfoDto) throws IllegalArgumentException{
        if (!userInfoDto.getEmail().isEmpty()) {
            if (!UserInfoInputValidation.isEmailValid(userInfoDto.getEmail())){
                throw new IllegalArgumentException("Invalid email format.");
            }
        }

        if (!UserInfoInputValidation.isPasswordValid(userInfoDto.getPassword())){
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain one uppercase letter, one lowercase letter, one digit, and one special character.");
        }

        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if (Objects.nonNull(checkIfUserAlreadyExist(userInfoDto.getUsername()))){
            return false;
        }
        String userId = UUID.randomUUID().toString();
        userInfoRepository.save(new UserInfo(userId,
                userInfoDto.getUsername(),
                userInfoDto.getPassword(),
                new HashSet<>()));
        return true;
    }
}
