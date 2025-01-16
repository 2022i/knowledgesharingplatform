package com.back.login;

import org.springframework.stereotype.Service;

@Service
public class LoginByEmail extends Login {
    @Override
    protected boolean isUserExist(String email) {
        return userRepository.findUserByEmail(email) != null;
    }

    @Override
    protected String getUserPassword(String email) {
        return userRepository.findUserByEmail(email).getPassword();
    }
    @Override
    protected int getUserId(String email) {
        return userRepository.findUserByEmail(email).getId();
    }
    @Override
    protected String getUsername(String userUniqueIdentifier) {
        return userRepository.findUserByEmail(userUniqueIdentifier).getUsername();
    }
}
