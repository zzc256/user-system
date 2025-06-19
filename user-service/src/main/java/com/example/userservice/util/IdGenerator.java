package com.example.userservice.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {
    private final Snowflake snowflake = IdUtil.getSnowflake(1, 1);
    public long nextId() {
        return snowflake.nextId();
    }
}
