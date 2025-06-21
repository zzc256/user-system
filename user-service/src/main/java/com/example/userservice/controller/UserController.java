package com.example.userservice.controller;


import com.example.common.PageResponseDTO;
import com.example.userservice.dto.*;
import com.example.userservice.service.UserService;
import com.example.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterDTO dto, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        userService.register(dto, ip); // 触发后续流程
        return ResponseEntity.ok("注册成功");
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserLoginDTO dto) {
        LoginResponseDTO response = userService.login(dto);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<PageResponseDTO<UserSimpleDTO>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestAttribute("userId") Long userId,
            @RequestAttribute("role") String role
    ) {
        return ResponseEntity.ok(userService.getUserList(page, size, userId, role));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserSimpleDTO> getUser(@PathVariable Long userId, HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        UserSimpleDTO dto = userService.getUserByIdWithPermission(userId, currentUserId, role);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId,
                                             @RequestBody @Valid UserUpdateDTO dto,
                                             HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("userId");
        String currentRole = (String) request.getAttribute("role");
        String ip = request.getRemoteAddr();

        userService.updateUserInfo(userId, dto, currentUserId, currentRole, ip);
        return ResponseEntity.ok("修改成功");
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetDTO dto,
                                                HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("userId");
        String currentRole = (String) request.getAttribute("role");
        String ip = request.getRemoteAddr();

        userService.resetPassword(dto, currentUserId, currentRole, ip);
        return ResponseEntity.ok("密码重置成功");
    }

}
