package com.nima.upquizz.service;

import com.nima.upquizz.entity.Authority;
import com.nima.upquizz.entity.User;
import com.nima.upquizz.repository.UserRepository;
import com.nima.upquizz.request.LoginRequest;
import com.nima.upquizz.request.RegisterRequest;
import com.nima.upquizz.response.TokenResponse;
import com.nima.upquizz.response.UserResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );
        User user = userRepository.getUserByUsername(loginRequest.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        String token = jwtService.generateToken(Map.of(), user);
        return new TokenResponse(token);
    }

    @Transactional
    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        boolean userExists = userRepository.getUserByUsername(registerRequest.username()).isPresent();

        if (userExists) {
            throw new IllegalArgumentException("Username is already in use");
        }
        User user = createUserFromRequest(registerRequest);
        return createUserResponse(userRepository.save(user));
    }

    private User createUserFromRequest(RegisterRequest registerRequest) {
        User user = new User();
        user.setId(0);
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setAuthorities(List.of(new Authority("ROLE_USER")));
        return user;
    }

    private UserResponse createUserResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getAuthorities().stream().map(it -> new Authority(it.getAuthority())).toList(),
                0,
                0
        );
    }
}
