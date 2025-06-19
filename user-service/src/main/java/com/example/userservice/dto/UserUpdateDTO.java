package com.example.userservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserUpdateDTO {
    @NotBlank
    private String username;

    @Email
    private String email;

    private String phone;
}
