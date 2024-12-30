package com.back.get.UserIdsList;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FansIdsList extends UserIdsList {
    @Override
    public List<Integer> getIdsList(int userId) {
        return userDataRepository.findUserDataById(userId).getFansId();
    }
}
