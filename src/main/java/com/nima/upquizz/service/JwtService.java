package com.nima.upquizz.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String extractUsername(String token);
    String generateToken(Map<String, Claims> claims, UserDetails userDetails);
    boolean validateToken(String token, UserDetails userDetails);
}
