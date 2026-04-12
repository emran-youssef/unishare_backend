// DTO carrying the credentials a student submits to log in
package com.unishare.unishare.dtos.auth;

import com.unishare.unishare.validation.EduEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "University email is required")
    @EduEmail
    private String universityEmail;

    @NotBlank(message = "Password is required")
    private String password;
}
