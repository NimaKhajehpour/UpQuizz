package com.nima.upquizz.controller;

import com.nima.upquizz.entity.User;
import com.nima.upquizz.request.LoginRequest;
import com.nima.upquizz.request.RegisterRequest;
import com.nima.upquizz.response.TokenResponse;
import com.nima.upquizz.response.UserResponse;
import com.nima.upquizz.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "authentication endpoint", description = "endpoint used to access authentication")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "login a user")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    TokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @Operation(summary = "register a user")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authenticationService.register(registerRequest);
    }
}
