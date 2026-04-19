// Handles registration and login business logic — the only place in the app that creates users and issues tokens
package com.unishare.unishare.services;

import com.unishare.unishare.dtos.auth.JwtResponse;
import com.unishare.unishare.dtos.auth.LoginRequest;
import com.unishare.unishare.dtos.auth.RegisterRequest;
import com.unishare.unishare.exceptions.User.EmailAlreadyExistsException;
import com.unishare.unishare.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private UserService userService;


    //sign-up
    public JwtResponse register(RegisterRequest request){

        if(userRepository.existsByUniversityEmail(request.getUniversityEmail())){
            throw new EmailAlreadyExistsException(
                    "Email already exist: "+ request.getUniversityEmail() );

        }

        var user = userService.createUser(request);
        String token = jwtService.generateToken(user);

        return new JwtResponse(
                token,
                user.getId(),
                user.getUniversityEmail(),
                user.getRole()
        );
    }

    //login
    public JwtResponse login(LoginRequest request){

        // Throws BadCredentialsException if wrong — GlobalExceptionHandler maps it to 401
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUniversityEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.
                findByUniversityEmail(request.getUniversityEmail())
                        .orElseThrow(()-> new RuntimeException("User not found!"));

        String token = jwtService.generateToken(user);

        return new JwtResponse(
                token,
                user.getId(),
                user.getUniversityEmail(),
                user.getRole()
        );

    }
}
