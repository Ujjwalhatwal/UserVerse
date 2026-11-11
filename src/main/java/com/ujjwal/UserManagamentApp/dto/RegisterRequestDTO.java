package com.ujjwal.UserManagamentApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3,max=40,message = "Username must be between 3 and 40 characters")
    private String username;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Size(max=100,message = "Email must be less than 100 characters")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8,max=15,message = "Password must be between 6 and 100 characters")
    private String password;
}
