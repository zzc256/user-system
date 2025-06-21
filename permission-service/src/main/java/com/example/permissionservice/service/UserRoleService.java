package com.example.permissionservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.permissionservice.entity.UserRole;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {
    String getUserRoleCode(Long userId);
    List<Long> getUserIdsByRole(String roleCode);
    void bindDefaultRole(Long userId);
}
