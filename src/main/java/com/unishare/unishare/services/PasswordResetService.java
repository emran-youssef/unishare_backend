package com.unishare.unishare.services;

import com.unishare.unishare.entities.PasswordResetToken;
import com.unishare.unishare.exceptions.User.UserNotFoundException;
import com.unishare.unishare.repositories.ResetPasswordTokenRepository;
import com.unishare.unishare.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final ResetPasswordTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void requestReset(String uniEmail){

        var user = userRepository.findByUniversityEmail(uniEmail)
                .orElseThrow(()-> new UserNotFoundException("User not found: " + uniEmail));

        // invalidate any previous unused OTPs for this user
        tokenRepository.invalidatePreviousToken(user.getId());

        // generate 6-digit OTP
        String otp = String.format("%06d", new SecureRandom().nextInt(1_000_000));

        //hash OTP
        String codeHash = sha256(otp);

        var token = PasswordResetToken.builder()
                .user(user)
                .codeHash(codeHash)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .build();

        tokenRepository.save(token);
        emailService.sendEmail(uniEmail, otp);
    }

    @Transactional
    public void resetPassword(String uniEmail, String otp, String newPassword){

        var user = userRepository.findByUniversityEmail(uniEmail)
                .orElseThrow(()-> new UserNotFoundException("User not found: " + uniEmail));

        String codeHash = sha256(otp);

        var token = tokenRepository.findValidToken(codeHash, user.getId(), LocalDateTime.now())
                .orElseThrow(()-> new IllegalArgumentException("Invalid or expired token"));

        token.setUsed(true);
        tokenRepository.save(token);

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // helper:  hashes the OTP before storing/comparing it.
    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

}
