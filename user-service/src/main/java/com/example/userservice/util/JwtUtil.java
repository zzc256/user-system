package com.example.userservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key"; // 可替换为更安全的秘钥

    // 生成 token
    public static String generateToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(userId.toString())             // userId 放在 subject
                .claim("role", role)                       // 自定义字段：角色
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS))) // token 7天过期
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 解析 token
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // 提取 userId
    public static Long getUserId(String token) {
        return Long.valueOf(parseToken(token).getSubject());
    }

    // 提取角色
    public static String getRole(String token) {
        return parseToken(token).get("role", String.class);
    }
}
