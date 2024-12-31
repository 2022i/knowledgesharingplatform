package com.back.get.UserDataList;

import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class UsersList {
    @Resource
    protected UserDataRepository userDataRepository;
    @Resource
    protected ArticleRepository articleRepository;
    protected List<UserData> getUserDataList(List<Integer> userIds) {
        List<UserData> userData = new ArrayList<>();
        for(Integer id : userIds){
            userData.add(userDataRepository.findUserDataById(id));
        }
        return userData;
    }
    abstract List<UserData> getUserDataList(int id);
}
