package com.unishare.unishare.controllers;

import com.unishare.unishare.dtos.auth.ResetPasswordRequest;
import com.unishare.unishare.services.PasswordResetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Register and login")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String uniEmail) {
        passwordResetService.requestReset(uniEmail);
        return ResponseEntity.noContent().build();  // 204
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(
                request.getUniversityEmail(),
                request.getOtp(),
                request.getNewPassword());
        return ResponseEntity.noContent().build();  //204
    }


}
