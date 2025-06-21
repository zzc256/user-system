package com.example.permissionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.permissionservice.entity.UserRole;
import com.example.permissionservice.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public String getUserRoleCode(Long userId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserRole userRole = userRoleMapper.selectOne(queryWrapper);
        return userRole != null ? userRole.getRoleCode() : null;
    }

    @Override
    public void bindDefaultRole(Long userId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleCode("user");
        userRole.setGmtCreate(LocalDateTime.now());
        userRoleMapper.insert(userRole);
    }

    @Override
    public void upgradeToAdmin(Long userId) {
        UpdateWrapper<UserRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId).set("role_code", "admin");
        userRoleMapper.update(null, updateWrapper);
    }

    @Override
    public void downgradeToUser(Long userId) {
        UpdateWrapper<UserRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId).set("role_code", "user");
        userRoleMapper.update(null, updateWrapper);
    }
}