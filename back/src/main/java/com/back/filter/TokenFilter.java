package com.back.filter;

import cn.hutool.core.util.StrUtil;
import com.back.redis.RedisCacheServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Token 过滤器
 * 1、从 token 中获取主体名称，即账号-username
 * 2、根据账号从缓存中获取权限列表-方法级权限控制
 *
 * @author lzz
 */
@Slf4j
@Component
public class TokenFilter extends OncePerRequestFilter {
    @Resource
    private RedisCacheServiceImpl rcs;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("X-Token");
        System.out.println("从缓存中得到admin");
        // 有token-不需要再username-password认证，否则需要
        if (StrUtil.isNotEmpty(token)) {
            //从缓存中得到token对应的admin
            String storedUsername = rcs.get(token);
            if(storedUsername!=null){
                // TODO 从token中获取主体名称，再从缓存中获取主体权限
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("user:query");
                authorities.add(authority);

                Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(storedUsername, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }


}
