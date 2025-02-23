package com.back.service.impl;

import com.back.index.User;
import com.back.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * UserDetailsService 实现类，供 AuthenticationManager 使用
 *
 * @author Damn
 */
@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {
    @Resource
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return getUserDetails(user);
    }

    /**
     * 获取用户详情
     *
     * @param user 用户信息
     * @return 用户详情
     */
    private UserDetails getUserDetails(User user) {
        return new UserDetails() {
            /**
             * 当前用户授权
             * @return 授权列表
             */
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                // TODO 待实现
                return List.of();
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
            }

            /**
             * 账号是否未过期
             * @return true-未过期，false-过期
             */
//            @Override
//            public boolean isAccountNonExpired() {
//                // 账号过期时间不小于当前时间表示未过期
//                int result = DateUtil.compare(user.getAccountExpiredTime(), new Date());
//                return result >= 0;
//            }

            /**
             * 账号是否未锁定
             * @return true-未锁定，false-已锁定
             */
//            @Override
//            public boolean isAccountNonLocked() {
//                return user.getLockFlag() == 0;
//            }

            /**
             * 密码是否未过期
             * @return true-未过期，false-过期
             */
//            @Override
//            public boolean isCredentialsNonExpired() {
//                // 密码过期时间不小于当前时间表示未过期
//                int result = DateUtil.compare(user.getCredentialsExpiredTime(), new Date());
//                return result >= 0;
//            }

            /**
             * 账号是否可用
             * @return true-可用，false-不可用
             */
//            @Override
//            public boolean isEnabled() {
//                return user.getStatus() == 0;
//            }
        };
    }
}
