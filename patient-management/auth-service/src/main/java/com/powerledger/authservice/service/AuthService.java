package com.powerledger.authservice.service;

import com.powerledger.authservice.dto.LoginRequestDTO;
import com.powerledger.authservice.model.User;
import com.powerledger.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        Optional<User> userOptional = userService.findByEmail(loginRequestDTO.getEmail());

        if (userOptional.isEmpty()) {
            System.out.println("User not found for email: " + loginRequestDTO.getEmail());
            return Optional.empty();
        }

        User user = userOptional.get();

        boolean matches = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());
        System.out.println("Password matches: " + matches);

        if (!matches) {
            return Optional.empty();
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        System.out.println("Generated token: " + token);

        return Optional.of(token);

    }

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e){
            return false;
        }
    }
}
