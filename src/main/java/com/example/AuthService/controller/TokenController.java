package com.example.AuthService.controller;

import com.example.AuthService.entities.RefreshToken;
import com.example.AuthService.request.AuthRequestDto;
import com.example.AuthService.request.RefreshTokenRequestDto;
import com.example.AuthService.response.JWTResponseDto;
import com.example.AuthService.service.JwtService;
import com.example.AuthService.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class TokenController {
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    /**
     * Called when both tokens are expired
     */
    @PostMapping("auth/v1/login")
    public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDto authRequestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword())
            );
            if (authentication.isAuthenticated()) {
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDto.getUsername());
                return new ResponseEntity(JWTResponseDto.builder()
                        .accessToken(jwtService.generateToken(authRequestDto.getUsername()))
                        .token(refreshToken.getToken())
                        .build(), HttpStatus.OK);
            }else {
                return new ResponseEntity("Could not authenticate", HttpStatus.BAD_REQUEST);
            }
        } catch (AuthenticationException e) {
            log.error("Error: {}", e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Called when access token is expired but refresh token is valid and alive
     */
//    @PostMapping("auth/v1/refreshToken")
//    public JWTResponseDto refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
//        return refreshTokenService.findByToken(refreshTokenRequestDto.getToken())
//                .map(refreshTokenService::verifyExpiration)
//                .map(RefreshToken::getUserInfo)
//                .map(userInfo -> {
//                    String accessToken = jwtService.generateToken(userInfo.getUsername());
//                    return JWTResponseDto.builder()
//                            .accessToken(accessToken)
//                            .token(refreshTokenRequestDto.getToken())
//                            .build();
//                }).orElseThrow(() -> new RuntimeException("Refresh token invalid."));
//    }
    @PostMapping("auth/v1/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        try {
            return refreshTokenService.findByToken(refreshTokenRequestDto.getToken())
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUserInfo)
                    .map(userInfo -> ResponseEntity.ok(JWTResponseDto.builder()
                            .accessToken(jwtService.generateToken(userInfo.getUsername()))
                            .token(refreshTokenRequestDto.getToken())
                            .build()))
                    .orElseThrow(() -> new RuntimeException("Refresh token invalid."));
        } catch (Exception e) {
            log.error("Unexpected exception: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
