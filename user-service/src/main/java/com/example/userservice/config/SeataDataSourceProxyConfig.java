package com.example.userservice.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SeataDataSourceProxyConfig {
    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        // 初始化原始数据源
        DruidDataSource originalDataSource = properties.initializeDataSourceBuilder()
                .type(DruidDataSource.class)
                .build();

        // 返回代理数据源
        return new DataSourceProxy(originalDataSource);
    }
}
