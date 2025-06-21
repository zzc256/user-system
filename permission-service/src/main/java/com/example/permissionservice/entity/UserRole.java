package com.example.permissionservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_role")
public class UserRole {
    private Long id;
    private Long userId;
    private String roleCode;
    private LocalDateTime gmtCreate;
}
