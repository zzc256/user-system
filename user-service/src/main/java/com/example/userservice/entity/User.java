package com.example.userservice.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("users") // 映射表名
public class User {

    @TableId(type = IdType.INPUT)
    private Long userId;

    private String username;
    private String password;
    private String email;
    private String phone;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
}
