package com.example.userservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class JwtInterceptor implements HandlerInterceptor {
    // 登录接口等白名单放行
    private static final String[] WHITELIST = {
            "/users/login", "/users/register"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        for (String allow : WHITELIST) {
            if (path.contains(allow)) return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            response.getWriter().write("缺少或非法的Token");
            return false;
        }

        try {
            Claims claims = JwtUtil.parseToken(token.replace("Bearer ", ""));
            request.setAttribute("userId", claims.get("userId"));
            request.setAttribute("role", claims.get("role"));
            return true;
        } catch (JwtException e) {
            response.setStatus(401);
            response.getWriter().write("Token无效或过期");
            return false;
        }
    }
}
