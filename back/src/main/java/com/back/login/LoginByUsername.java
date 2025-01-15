package com.back.login;

import org.springframework.stereotype.Service;

@Service
public class LoginByUsername extends Login {
    @Override
    protected boolean isUserExist(String username) {
        return userRepository.findUserByUsername(username) != null;
    }
    @Override
    protected String getUserPassword(String username) {
        return userRepository.findUserByUsername(username).getPassword();
    }
    @Override
    protected int getUserId(String username) {
        return userRepository.findUserByUsername(username).getId();
    }
}
