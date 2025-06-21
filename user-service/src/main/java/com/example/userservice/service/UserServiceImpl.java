package com.example.userservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.OperationLogEvent;
import com.example.common.PageResponseDTO;
import com.example.userservice.client.PermissionFeignClient;
import com.example.userservice.dto.*;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.mq.LogMessageProducer;
import com.example.userservice.util.IdGenerator;
import com.example.userservice.util.JwtUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PermissionFeignClient permissionFeignClient;
    @Autowired
    private LogMessageProducer logMessageProducer;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    @GlobalTransactional(name = "user-register", rollbackFor = Exception.class)
    public void register(UserRegisterDTO dto, String ip) {
        Long userId = idGenerator.nextId();

        User user = new User();
        user.setUserId(userId);
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setGmtCreate(LocalDateTime.now());
        this.save(user); // 分库分表插入用户
        permissionFeignClient.bindDefaultRole(userId); // RPC绑定默认角色
        logMessageProducer.sendLog(new OperationLogEvent(
                userId, "用户注册", ip, "注册用户名: " + dto.getUsername()
        )); // MQ发送日志
    }

    @Override
    public LoginResponseDTO login(UserLoginDTO dto) {
        // 1. 查询用户
        User user = this.lambdaQuery()
                .eq(User::getUsername, dto.getUsername())
                .one();

        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }
        // 2. 校验密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        // 3. 查询角色（调用权限服务）
        String role = permissionFeignClient.getUserRoleCode(user.getUserId());
        // 4. 生成 JWT
        String token = JwtUtil.generateToken(user.getUserId(), role);
        return new LoginResponseDTO(token);
    }
    @Override
    public PageResponseDTO<UserSimpleDTO> getUserList(int page, int size, Long currentUserId, String role) {
        Page<User> userPage = new Page<>(page, size);
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        switch (role) {
            case "SUPER_ADMIN":
                // 超管查看所有用户，不需要加条件
                break;
            case "ADMIN":
                List<Long> userIds = permissionFeignClient.getUserIdsByRole("USER");
                if (userIds == null || userIds.isEmpty()) {
                    return new PageResponseDTO<>(0L, new java.util.ArrayList<>());
                }
                wrapper.in("user_id", userIds);
                break;
            case "USER":
                wrapper.eq("user_id", currentUserId);
                break;
            default:
                throw new RuntimeException("无效角色权限: " + role);
        }

        Page<User> resultPage = this.page(userPage, wrapper);

        List<UserSimpleDTO> dtos = new java.util.ArrayList<>();
        for (User user : resultPage.getRecords()) {
            UserSimpleDTO dto = new UserSimpleDTO();
            dto.setUserId(user.getUserId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setGmtCreate(user.getGmtCreate().toString());
            dtos.add(dto);
        }

        return new PageResponseDTO<>(resultPage.getTotal(), dtos);
    }

    @Override
    public UserSimpleDTO getUserByIdWithPermission(Long targetUserId, Long currentUserId, String role) {
        // 权限校验
        switch (role) {
            case "SUPER_ADMIN":
                break; // 超管无限制
            case "ADMIN":
                String targetRole = permissionFeignClient.getUserRoleCode(targetUserId);
                if (!"USER".equals(targetRole)) {
                    throw new RuntimeException("管理员只能查询普通用户");
                }
                break;
            case "USER":
                if (!currentUserId.equals(targetUserId)) {
                    throw new RuntimeException("普通用户只能查询自己");
                }
                break;
            default:
                throw new RuntimeException("无效角色");
        }

        // 查询用户
        User user = this.getById(targetUserId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 转换为 DTO
        UserSimpleDTO dto = new UserSimpleDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setGmtCreate(user.getGmtCreate().toString());
        return dto;
    }
    @Override
    @Transactional
    public void updateUserInfo(Long targetUserId, UserUpdateDTO dto, Long currentUserId, String currentRole, String ip) {
        // 权限判断
        if ("USER".equals(currentRole) && !targetUserId.equals(currentUserId)) {
            throw new RuntimeException("普通用户无权修改他人信息");
        }
        if ("ADMIN".equals(currentRole)) {
            String targetRole = permissionFeignClient.getUserRoleCode(targetUserId);
            if (!"USER".equals(targetRole)) {
                throw new RuntimeException("管理员只能修改普通用户");
            }
        }

        // 执行更新
        User user = new User();
        user.setUserId(targetUserId);
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        this.updateById(user);

        // 日志
        logMessageProducer.sendLog(new OperationLogEvent(
                currentUserId,
                "修改用户信息",
                ip,
                "修改用户ID: " + targetUserId + "，修改为用户名：" + dto.getUsername()
        ));
    }
    @Override
    public void resetPassword(PasswordResetDTO dto, Long currentUserId, String currentRole, String ip) {
        Long targetUserId = dto.getUserId();

        // 权限判断
        if ("USER".equals(currentRole) && !targetUserId.equals(currentUserId)) {
            throw new RuntimeException("普通用户只能重置自己的密码");
        }

        if ("ADMIN".equals(currentRole)) {
            String targetRole = permissionFeignClient.getUserRoleCode(targetUserId);
            if (!"USER".equals(targetRole)) {
                throw new RuntimeException("管理员只能重置普通用户的密码");
            }
        }

        // 修改密码
        String encoded = passwordEncoder.encode(dto.getNewPassword());
        boolean updated = this.lambdaUpdate()
                .eq(User::getUserId, targetUserId)
                .set(User::getPassword, encoded)
                .update();

        if (!updated) {
            throw new RuntimeException("密码更新失败，用户可能不存在");
        }

        // 发送日志
        logMessageProducer.sendLog(new OperationLogEvent(
                currentUserId,
                "重置用户密码",
                ip,
                "目标用户ID: " + targetUserId
        ));
    }


}
