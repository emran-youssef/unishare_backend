// Safe outbound representation of a user — never exposes passwordHash or internal fields
package com.unishare.unishare.dtos.user;

import com.unishare.unishare.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDto {

    private Long id;
    private String fullName;
    private String universityEmail;
    private Role role;
    private String profilePicture;
    private String phone;
    private LocalDateTime createdAt;

}
