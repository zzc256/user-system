package com.example.loggingservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.loggingservice.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
