// DTO carrying the data a new student submits to create an account
package com.unishare.unishare.dtos.auth;

import com.unishare.unishare.validation.EduEmail;
import com.unishare.unishare.validation.Lowercase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must be at most 100 characters")
    private String fullName;

    @EduEmail
    @NotBlank(message = "university email required")
    @Lowercase(message = "University email must be lowercase")
    private String universityEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

}
