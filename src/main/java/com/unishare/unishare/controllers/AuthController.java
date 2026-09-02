package com.unishare.unishare.controllers;

import com.unishare.unishare.dtos.auth.JwtResponse;
import com.unishare.unishare.dtos.auth.LoginRequest;
import com.unishare.unishare.dtos.auth.RegisterRequest;
import com.unishare.unishare.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Authentication", description = "Register and login")
public class AuthController {

    private AuthService authService;

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
}
