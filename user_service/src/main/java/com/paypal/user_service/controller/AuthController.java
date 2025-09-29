package com.paypal.user_service.controller;

import com.paypal.user_service.Util.JwtUtil;
import com.paypal.user_service.dto.JwtResponse;
import com.paypal.user_service.dto.SignupRequest;
import com.paypal.user_service.dto.loginRequest;
import com.paypal.user_service.entity.User;
import com.paypal.user_service.repository.UserRepository;
import com.paypal.user_service.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if(existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("User already Exists!");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok().body("✅ User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody loginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if(user.isEmpty()){
            return ResponseEntity.status(401).body("User Not Found!");
        }
        User loggedInUser = user.get();
        //Compare DB password and requesting password
        if(!passwordEncoder.matches(loginRequest.getPassword(), loggedInUser.getPassword())){
            return ResponseEntity.status(401).body("Incorrect Password!");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", loggedInUser.getRole());

        String token = jwtUtil.generateToken(claims, loggedInUser.getEmail());

        return ResponseEntity.ok(new JwtResponse(token));
    }
}


