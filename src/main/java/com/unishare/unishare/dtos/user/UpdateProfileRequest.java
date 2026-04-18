// Fields the client is allowed to change — role and email are intentionally excluded
package com.unishare.unishare.dtos.user;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @Size(max = 100, message = "Full name must be at most 100 characters")
    private String fullName;

    @Size(max = 500, message = "Profile picture URL must be at most 500 characters")
    private String profilePicture;

    @Size(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

}
