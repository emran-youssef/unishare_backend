package com.unishare.unishare.services;

import com.unishare.unishare.dtos.auth.RegisterRequest;
import com.unishare.unishare.entities.User;
import com.unishare.unishare.enums.Role;
import com.unishare.unishare.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User createUser(RegisterRequest request) {
        return userRepository.save(
                User.builder()
                        .fullName(request.getFullName())
                        .universityEmail(request.getUniversityEmail())
                        .passwordHash(passwordEncoder.encode(request.getPassword()))
                        .role(Role.STUDENT)
                        .build()
        );
    }
}
