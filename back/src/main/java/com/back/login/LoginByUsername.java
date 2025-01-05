package com.back.login;

import org.springframework.stereotype.Service;

@Service
public class LoginByUsername extends Login {
    @Override
    protected boolean isUserExist(String username) {
        return userRepository.findUserByUsername(username) != null;
    }
    @Override
    protected String getPassword(String username) {
        return userRepository.findUserByUsername(username).getPassword();
    }
}
