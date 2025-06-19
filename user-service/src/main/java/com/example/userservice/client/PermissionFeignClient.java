package com.example.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "permission-service", url = "http://localhost:8081", path = "/permission")
public interface PermissionFeignClient {
    // 绑定默认角色（在注册中已用到）
    @PostMapping("/bind-default-role")
    void bindDefaultRole(@RequestParam("userId") Long userId);
    // 查询用户角色码
    @GetMapping("/get-role-code")
    String getUserRoleCode(@RequestParam("userId") Long userId);

    @PostMapping("/upgrade-to-admin")
    void upgradeToAdmin(@RequestParam("userId") Long userId);

    @PostMapping("/downgrade-to-user")
    void downgradeToUser(@RequestParam("userId") Long userId);

    @GetMapping("/internal/role/users")
    List<Long> getUserIdsByRole(@RequestParam("roleCode") String roleCode);



}
