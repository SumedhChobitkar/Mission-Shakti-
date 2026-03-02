package com.Mission.Shakti.dto;

import com.Mission.Shakti.Enum.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserDto {

    @NotBlank(message = "Username is required")
    @Size(min=4, max=20, message = "Username must be of 4-20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
            message = "Password must contain uppercase, lowercase and number"
    )
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    private Role role;
}
