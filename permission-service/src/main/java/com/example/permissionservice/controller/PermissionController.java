package com.example.permissionservice.controller;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")

public class PermissionController {
    @PostMapping("/bind-default-role")
    public void bindDefaultRole(@RequestParam Long userId) {

    }

    @GetMapping("/get-role-code")
    public void getUserRoleCode(@RequestParam Long userId) {
           return  ;
    }
}
