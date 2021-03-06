package ru.example.todo.service.impl;
/*
 * Date: 3/25/21
 * Time: 4:39 PM
 * */

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.todo.config.properties.TokenProperties;
import ru.example.todo.entity.RefreshToken;
import ru.example.todo.entity.User;
import ru.example.todo.exception.CustomException;
import ru.example.todo.repository.UserRepository;
import ru.example.todo.security.UserDetailsImpl;
import ru.example.todo.service.JwtTokenService;
import ru.example.todo.service.RefreshTokenService;
import ru.example.todo.service.UserService;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends AbstractServiceClass implements UserService {

    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final TokenProperties tokenProperties;

    public UserServiceImpl(JwtTokenService jwtTokenService, UserRepository userRepository,
                           TokenProperties tokenProperties, AuthenticationManager authManager,
                           BCryptPasswordEncoder bCryptPasswordEncoder, RefreshTokenService refreshTokenService) {
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.tokenProperties = tokenProperties;
        this.authManager = authManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public Map<String, String> login(User user, String ip) {
        try {
            Authentication auth = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            User userFromDb = ((UserDetailsImpl) auth.getPrincipal()).getUser();
            return buildResponseBody(userFromDb, ip);
        } catch (AuthenticationException ex) {
            throw new CustomException("Username Not Found / Incorrect Password", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public String register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new CustomException("Username already in use", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "ok";
    }

    @Override
    public Map<String, String> generateNewTokens(String refreshToken, String ip) {
        RefreshToken oldRefreshToken = refreshTokenService.findRefreshTokenByValue(refreshToken);

        User user = userRepository.findById(oldRefreshToken.getUserId()).orElseThrow(() ->
                new CustomException("Refresh token owner not found", HttpStatus.BAD_REQUEST));

        return buildResponseBody(user, ip); // generate new access and refresh tokens
    }

    @Override
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException("User Not Found: " + userId, HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User Not Found", HttpStatus.NOT_FOUND));
    }

    @Override
    public void updatePassword(String email, String password) {
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new CustomException("Username Not Found", HttpStatus.BAD_REQUEST));

        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String email) {
        return userRepository.existsByUsername(email);
    }

    Map<String, String> buildResponseBody(User user, String ip) {
        String accessToken = jwtTokenService.buildAccessToken(user.getId(), user.getRoles());
        String refreshToken = refreshTokenService.createRefreshToken(user.getId(), ip);

        return Collections.unmodifiableMap(new LinkedHashMap<>() {{
            put("access_token", accessToken);
            put("refresh_token", refreshToken);
            put("token_type", "Bearer");
            put("access_token_expires", String.valueOf(tokenProperties.getAccessTokenValidity()));
            put("refresh_token_expires", String.valueOf(tokenProperties.getRefreshTokenValidity()));
        }});
    }

}
