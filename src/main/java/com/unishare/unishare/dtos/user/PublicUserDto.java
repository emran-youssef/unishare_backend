package com.unishare.unishare.dtos.user;

import com.unishare.unishare.enums.Role;
import lombok.Data;

@Data
public class PublicUserDto {

    private Long id;
    private String fullName;
    private String universityEmail;
    private Role role;
    private String profilePicture;

}
