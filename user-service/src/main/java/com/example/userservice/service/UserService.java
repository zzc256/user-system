package com.example.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.PageResponseDTO;
import com.example.userservice.dto.*;
import com.example.userservice.entity.User;


public interface UserService extends IService<User> {
        void register(UserRegisterDTO dto, String ip);
        LoginResponseDTO login(UserLoginDTO dto);
        PageResponseDTO<UserSimpleDTO> getUserList(int page, int size, Long userId, String role);
        UserSimpleDTO getUserByIdWithPermission(Long targetUserId, Long currentUserId, String role);
        void updateUserInfo(Long targetUserId, UserUpdateDTO dto, Long currentUserId, String currentRole, String ip);
        void resetPassword(PasswordResetDTO dto, Long currentUserId, String currentRole, String ip);



}

