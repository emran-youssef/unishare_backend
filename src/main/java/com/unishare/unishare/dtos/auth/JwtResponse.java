package com.unishare.unishare.dtos.auth;

import com.unishare.unishare.enums.Role;
import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long userId;
    private String universityEmail;
    private Role role;

    public JwtResponse(String token, Long userId, String universityEmail, Role role) {
        this.token = token;
        this.userId = userId;
        this.universityEmail = universityEmail;
        this.role = role;
    }
}
