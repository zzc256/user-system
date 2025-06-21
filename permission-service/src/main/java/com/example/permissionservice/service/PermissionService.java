package com.example.permissionservice.service;

public interface PermissionService {
    String getUserRoleCode(Long userId);
    void bindDefaultRole(Long userId);
    void upgradeToAdmin(Long userId);
    void downgradeToUser(Long userId);
}
