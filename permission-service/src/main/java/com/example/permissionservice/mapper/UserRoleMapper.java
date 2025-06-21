package com.example.permissionservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.permissionservice.entity.UserRole;
import feign.Param;
import org.apache.ibatis.annotations.*;

@Mapper

public interface UserRoleMapper extends BaseMapper<UserRole> {
    // 不需要写任何方法，基础的插入、查询、更新都由 MyBatis-Plus 提供
}
