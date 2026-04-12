package com.unishare.unishare.controllers;

import com.unishare.unishare.dtos.auth.JwtResponse;
import com.unishare.unishare.dtos.auth.LoginRequest;
import com.unishare.unishare.dtos.auth.RegisterRequest;
import com.unishare.unishare.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(
            @Valid @RequestBody RegisterRequest request
            ){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));

    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request
    ){

        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/test-hash")
    public String testHash() {
        return passwordEncoder.encode("00000000");
    }
}
