package com.example.AuthService.repository;

import com.example.AuthService.entities.RefreshToken;
import com.example.AuthService.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    public Optional<RefreshToken> findByToken(String token);

    public Optional<RefreshToken> findByUserInfo(UserInfo userInfo);
}
