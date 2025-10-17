package com.paypal.user_service.controller;

import com.paypal.user_service.dto.JwtResponse;
import com.paypal.user_service.dto.loginRequest;
import com.paypal.user_service.dto.SignupRequest;
import com.paypal.user_service.entity.User;
import com.paypal.user_service.repository.UserRepository;
import com.paypal.user_service.service.UserService;
import com.paypal.user_service.Util.JwtUtil;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign up a new user", description = "Creates a new user and wallet")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        // 1. Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("⚠️ User already exists");
        }

        // 2. Map request -> User entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole("ROLE_USER"); // ✅ Better for Spring Security
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3. Use service to create user + wallet
        User savedUser = userService.createUser(user);

        // 4. Return safe response
        return ResponseEntity.ok("✅ User registered successfully with ID: " + savedUser.getId());
    }






    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "login")
    public ResponseEntity<?> login(@RequestBody loginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("❌ User not found");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("❌ Invalid credentials");
        }

        // Generate token with claims
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        return ResponseEntity.ok(new JwtResponse(token));
    }

}


