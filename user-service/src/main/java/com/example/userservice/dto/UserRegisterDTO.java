package com.example.userservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserRegisterDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

    private String phone;
}
