package com.nima.upquizz.service;

import com.nima.upquizz.entity.User;
import com.nima.upquizz.request.LoginRequest;
import com.nima.upquizz.request.RegisterRequest;
import com.nima.upquizz.response.TokenResponse;
import com.nima.upquizz.response.UserResponse;

import java.util.Map;

public interface AuthenticationService {
    TokenResponse login(LoginRequest loginRequest);
    UserResponse register(RegisterRequest registerRequest);
}
