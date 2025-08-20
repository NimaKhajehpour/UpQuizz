package com.nima.upquizz.config;

import com.nima.upquizz.entity.Authority;
import com.nima.upquizz.entity.User;
import com.nima.upquizz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class InitializeAdmin {

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.username}")
    private String adminUsername;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public InitializeAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Bean
    CommandLineRunner initAdmin(){
        return args -> {
            boolean adminExists = userRepository.existsByAuthorities(List.of(new Authority("ROLE_ADMIN")));

            if (!adminExists) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setAuthorities(List.of(new Authority("ROLE_ADMIN")));
                userRepository.save(admin);
            }
        };
    }
}
