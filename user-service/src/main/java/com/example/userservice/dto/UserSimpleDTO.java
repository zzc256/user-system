package com.example.userservice.dto;

import lombok.Data;

@Data
public class UserSimpleDTO {

    private Long userId;
    private String username;
    private String email;
    private String phone;
    private String gmtCreate;
}
