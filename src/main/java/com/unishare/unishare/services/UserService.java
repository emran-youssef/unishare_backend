package com.unishare.unishare.services;

import com.unishare.unishare.dtos.auth.RegisterRequest;
import com.unishare.unishare.dtos.user.ChangePasswordRequest;
import com.unishare.unishare.dtos.user.PublicUserDto;
import com.unishare.unishare.dtos.user.UpdateProfileRequest;
import com.unishare.unishare.dtos.user.UserDto;
import com.unishare.unishare.entities.User;
import com.unishare.unishare.enums.Role;
import com.unishare.unishare.exceptions.User.UserNotFoundException;
import com.unishare.unishare.mappers.UserMapper;
import com.unishare.unishare.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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


    // Returns the safe DTO of the currently authenticated user
    public UserDto getProfile(String universityEmail){

        var user  = userRepository.findByUniversityEmail(universityEmail)
                .orElseThrow(()->new UserNotFoundException(
                        "User not found: " + universityEmail));
        return userMapper.toDto(user);
    }



    // Updates only the fields the user is allowed to change+
    public UserDto updateProfile(String universityEmail,
                                 UpdateProfileRequest request){
        var user = userRepository.findByUniversityEmail(universityEmail)
                .orElseThrow(()-> new UserNotFoundException(
                        "User not found" + universityEmail ));

        if(request.getFullName() != null) user.setFullName(request.getFullName());
        if(request.getProfilePicture() != null) user.setProfilePicture(request.getProfilePicture());
        if(request.getPhone() != null) user.setPhone(request.getPhone());

        return userMapper.toDto(user);

    }

    // Verifies current password before replacing it
    public void changePassword(String universityEmail, ChangePasswordRequest request){

        var user = userRepository.findByUniversityEmail(universityEmail)
                .orElseThrow(()-> new UserNotFoundException("User not found" + universityEmail));

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())){
            throw new BadCredentialsException("Current Password incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public PublicUserDto getPublicProfile(Long userId){

        var user = userRepository.findById(userId)
                .orElseThrow(()->
                        new UserNotFoundException("User not found" + userId));

        return userMapper.toPublicDto(user);
    }


    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    public Long getIdByEmail(String universityEmail) {
        return userRepository.findByUniversityEmail(universityEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + universityEmail))
                .getId();
    }

    }







