package com.url_Shortner.demo.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Pattern(
            regexp = "^(?=.*[0-9])[a-zA-Z0-9_]+$",
            message = "Username must contain at least one number"
    )
    private String Username;
    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@(gmail|yahoo)\\.com$",
            message = "Email must be a Gmail or Yahoo address"
    )
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).+$",
            message = "Password must contain uppercase, lowercase, number and special character"
    )
    private String Password;
    private Set<String> role;

}
