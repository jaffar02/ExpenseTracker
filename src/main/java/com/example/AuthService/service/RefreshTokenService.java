package com.example.AuthService.service;

import com.example.AuthService.entities.RefreshToken;
import com.example.AuthService.entities.UserInfo;
import com.example.AuthService.repository.RefreshTokenRepository;
import com.example.AuthService.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    public RefreshToken createRefreshToken(String username) {
        UserInfo userInfoExtracted = userInfoRepository.findByUsername(username);
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserInfo(userInfoExtracted);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .userInfo(userInfoExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();

        if (existingToken.isPresent()) {
            existingToken.get().setToken(newRefreshToken.getToken());
            existingToken.get().setExpiryDate(Instant.now().plusMillis(600000));
            return refreshTokenRepository.save(existingToken.get());
        }else {
            return refreshTokenRepository.save(newRefreshToken);
        }
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) { //means expired token
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired, login again");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
